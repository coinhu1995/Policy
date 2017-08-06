package ptit.nhunh.prepare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ptit.nhunh.classification.Classifier;
import ptit.nhunh.context.Context;
import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 * Prepare data với chỉ 2 nhãn 1, 3 IDF của data test tính gộp cả training. Su
 * dung vnTokenizer
 * 
 * @author uhn
 *
 */
@SuppressWarnings("unused")
public class PrepareData4 {
	private VietTokenizer vietToken;
	private SQLDAO cmtTestDao;
	private SQLDAO cmtDao;
	private SQLDAO wordDao;

	private BufferedWriter bw;
	private BufferedWriter bw1;
	private BufferedWriter bw2;
	private BufferedWriter bw3;

	private ArrayList<Object> listTrainCmt;
	private ArrayList<Object> listTestCmt;

	private ArrayList<Word> listWord;

	private int labelCount = 2;

	public PrepareData4() throws SQLException, IOException {
		this.cmtTestDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
		this.cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.wordDao = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);
		// this.vietToken = new VietTokenizer();
		this.listWord = new ArrayList<>();

		String date = LocalDate.now().toString();
		String time = LocalTime.now().toString();

		String path = "src\\main\\resource\\data\\" + this.labelCount + "label\\"
				+ date.replaceAll("-", "") + "\\" + time.substring(0, 5).replace(":", "");
		BufferedWriter pathWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("path.txt"))));
		pathWriter.write(path);
		pathWriter.close();

		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		this.bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\train"))));
		this.bw1 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\test"))));
		this.bw2 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\input.train"))));
		this.bw3 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\input.test"))));

		this.listTrainCmt = this.cmtTestDao.getData(
				"select * from TblCommentTest where id <= " + Context.TRAINSIZE + " order by id");
		this.listTestCmt = this.cmtTestDao.getData(
				"select * from TblCommentTest where id <= " + (Context.TESTSIZE + Context.TRAINSIZE)
						+ " and id > " + Context.TRAINSIZE + " order by id");
		this.wordDao.update("delete from TblWord");
		this.wordDao.update("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
	}

	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException,
			InterruptedException, ClassNotFoundException {
		new PrepareData4().process();
		new Classifier().execute();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		System.out.println("\n--- Start Processing ---");

		long t1 = System.currentTimeMillis();
		this.genTrainingDataFile(Context.TRAINSIZE);
		long t2 = System.currentTimeMillis();

		this.genTestingDataFile(Context.TRAINSIZE, Context.TESTSIZE + Context.TRAINSIZE);
		long t3 = System.currentTimeMillis();

		System.out.println("--- End Processing ---");

		System.out.println("Generate Training File   : " + (t2 - t1) / (float) 60000);
		System.out.println("Generate Testing File   : " + (t3 - t2) / (float) 60000);
	}

	/**
	 * Tạo các file train data (input.train, train).
	 * 
	 * @param train
	 * @param test
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void genTrainingDataFile(int train)
			throws SQLException, IOException, InterruptedException {
		System.out.println("\t+> Training file Generating...");

		this.collect(this.listWord, this.listTrainCmt);
		this.checkStopWord(this.listWord);

		this.write(this.listTrainCmt, this.bw, this.bw2, Context.TRAINSIZE);
	}

	/**
	 * Tạo các file test data (input.test, test).
	 * 
	 * @param train
	 * @param test
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void genTestingDataFile(int train, int test)
			throws SQLException, IOException, InterruptedException {
		System.out.println("\t+> Testing file Generating...");

		this.collect(this.listWord, this.listTestCmt);
		this.checkStopWord(this.listWord);

		this.write(this.listTestCmt, this.bw1, this.bw3, Context.TRAINSIZE + Context.TESTSIZE);
	}

	/**
	 * Collect các từ trong <strong>rs</strong> mà chưa có trong
	 * <strong>listAllWord</strong>. Kết hợp tính DF của mỗi từ.
	 * 
	 * @param listWord
	 * @param rs
	 * @throws SQLException
	 */
	private void collect(ArrayList<Word> listWord, ArrayList<Object> listCmt) throws SQLException {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			String segmentCmt = c.getCmt_segment();

			ArrayList<Word> aw = Utils.sentence2Words(segmentCmt);
			for (int i = 0; i < aw.size(); i++) {
				int pos = Utils.indexOf(listWord, aw.get(i));
				if (pos == -1) {
					aw.get(i).setId(listWord.size() + 1);
					aw.get(i).setDF(1);
					aw.get(i).setId(listWord.size() + 1);
					aw.get(i).setCmt_id(c.getId());
					listWord.add(aw.get(i));
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}
	}

	/**
	 * <p>
	 * Chuyển các câu ở trong rs thành dạng vector và ghi ra file
	 * <strong>bw1</strong>, <strong>bw2</strong>.
	 * </p>
	 * 
	 * <strong>bw1</strong>: các câu có id ở đầu để map vào Database <br>
	 * <strong>bw2</strong>: các câu không có id nhưng map các dòng với bw1.
	 * 
	 * @param rs
	 * @param bw1
	 * @param bw2
	 * @throws SQLException
	 * @throws IOException
	 */
	public void write(ArrayList<Object> listCmt, BufferedWriter bw1, BufferedWriter bw2, int size)
			throws SQLException, IOException {
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			String show = "", line1 = "";
			show += c.getId() + " ";
			show += c.getLabel() + " ";
			line1 += c.getLabel() + " ";
			ArrayList<Word> words = Utils.sentence2Words(c.getCmt_segment());

			int numOfWord = c.getCmt_segment().split(" ").length;
			for (int i = 0; i < words.size(); i++) {
				int index = Utils.indexOf(this.listWord, words.get(i));
				if (index > -1) {
					words.get(i).setDF(this.listWord.get(index).getDF());
				}
				words.get(i).setId(this.listWord.get(index).getId());
			}

			Collections.sort(words, new Comparator<Word>() {
				@Override
				public int compare(Word o1, Word o2) {
					if (o1.getId() > o2.getId())
						return 1;
					if (o1.getId() < o2.getId())
						return -1;
					return 0;
				}
			});

			for (int i = 0; i < words.size(); i++) {
				if (words.get(i).getIsStop() != 1) {
					show += words.get(i).getWord() + ":" + words.get(i).getTimesOccur() + ":"
							+ words.get(i).getDF() + ":" + words.get(i).getTFIDF(size) + " ";
					line1 += words.get(i).getId() + ":" + words.get(i).getTFIDF(size) + " ";
				}
			}
			if (show.length() > 2) {
				bw1.write(show);
				bw1.write("\t\t" + c.getCmt_segment());
				bw1.newLine();
				bw2.write(line1);
				bw2.newLine();
			}
		}

		bw1.close();
		bw2.close();
	}

	private void checkStopWord(ArrayList<Word> listCmt) throws IOException, SQLException {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("stopword.txt")));
		String s = "";
		while ((s = br.readLine()) != null) {
			for (Word word : listCmt) {
				if (collator.equals(s, word.getWord())) {
					word.setIsStop(1);
					break;
				}
			}
		}
		br.close();
	}
}
