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

import org.apache.commons.lang3.SerializationUtils;

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
public class PrepareData4 {
	@SuppressWarnings("unused")
	private VietTokenizer vietToken;
	private SQLDAO cmtTestDao;
	private SQLDAO wordDAO;

	private Context context;

	private BufferedWriter bw;
	private BufferedWriter bw1;
	private BufferedWriter bw2;
	private BufferedWriter bw3;

	private ArrayList<Word> wordsOfTrainingData;
	private ArrayList<Word> wordsOfTrainingTestingData;

	private int labelCount = 2;

	public PrepareData4() throws SQLException, IOException {
		this.cmtTestDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
		this.wordDAO = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);
		this.vietToken = new VietTokenizer();
		this.context = Context.getInstance();

		String date = LocalDate.now().toString();
		String time = LocalTime.now().toString();

		String path = "src\\main\\resource\\data\\" + this.labelCount + "label\\"
				+ date.replaceAll("-", "") + "\\" + time.substring(0, 5).replace(":", "");

		System.out
				.println(date.replaceAll("-", "") + "\\\\" + time.substring(0, 5).replace(":", ""));

		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		this.bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\train.txt"))));
		this.bw1 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\test"))));
		this.bw2 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\input.train"))));
		this.bw3 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\input.test"))));

		this.wordsOfTrainingData = new ArrayList<>();

		this.wordDAO.update("delete from TblWord");
		this.wordDAO.update("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
	}

	public static void main(String[] args)
			throws FileNotFoundException, SQLException, IOException, InterruptedException {
		new PrepareData4().process();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		System.out.println("\n--- Start Processing ---");

		long sGenTrainDataFile = System.currentTimeMillis();
		this.genTrainingDataFile(this.context.getTrain());
		long eGenTrainDataFile = System.currentTimeMillis();

		this.genTestingDataFile(this.context.getTrain(), this.context.getTest());
		long eGenTestDataFile = System.currentTimeMillis();

		System.out.println("--- End Processing ---");

		System.out.println("Generate Training File   : "
				+ (eGenTrainDataFile - sGenTrainDataFile) / (float) 60000);
		System.out.println("Generate Testing File   : "
				+ (eGenTestDataFile - eGenTrainDataFile) / (float) 60000);
	}

	/**
	 * Tạo các file training data (input.train).
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
		this.collectTrainingWord(train);
		ArrayList<Object> listCmt = this.cmtTestDao.getData("select * from "
				+ this.context.getCommentTableName() + " where id <= " + train + " order by id ");

		this.write(listCmt, this.wordsOfTrainingData, this.bw, this.bw2);
	}

	private void genTestingDataFile(int train, int test)
			throws SQLException, IOException, InterruptedException {
		System.out.println("\t+> Testing file Generating...");
		ArrayList<Object> listCmt = this.cmtTestDao
				.getData("select * from " + this.context.getCommentTableName() + " where id > "
						+ train + " and id <= " + test + " order by id ");

		this.wordsOfTrainingTestingData = SerializationUtils.clone(this.wordsOfTrainingData);

		this.collect(this.wordsOfTrainingTestingData, listCmt);
		this.checkStopWord(this.wordsOfTrainingTestingData);
		for (int i = 0; i < this.wordsOfTrainingTestingData.size(); i++) {
			this.wordsOfTrainingTestingData.get(i)
					.setIDF(Math.log10((test)
							/ (float) this.wordsOfTrainingTestingData.get(i).getDF()));
		}

		this.write(listCmt, this.wordsOfTrainingTestingData, this.bw1, this.bw3);
	}

	/**
	 * Collect các từ ở trong limit comments đầu tiên. Và thêm vào Database.
	 * 
	 * @param train
	 * @throws SQLException
	 * @throws IOException
	 */
	private void collectTrainingWord(int train) throws SQLException, IOException {
		ArrayList<Word> listWord = new ArrayList<>();
		ArrayList<Object> listCmt = this.cmtTestDao.getData("select * from "
				+ this.context.getCommentTableName() + " where id <= " + train + " order by id");

		this.collect(listWord, listCmt);
		this.checkStopWord(listWord);
		// Thêm các từ đã tách được từ tập huấn luyện vào CSDL
		System.out.println(String.format("%25s", "Word") + String.format("%10s", "DF")
				+ String.format("%10s", "TF") + String.format("%20s", "IDF"));
		for (int i = 0; i < listWord.size(); i++) {
			try {
				// listWord.get(i).setIDF((float) Math.log10((train) / (1 +
				// listWord.get(i).getDF())));
				listWord.get(i).setIDF(Math.log10(
						(train) / (float) listWord.get(i).getDF()));
				this.wordDAO.insert(listWord.get(i));
				listWord.get(i).print();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.wordsOfTrainingData = SerializationUtils.clone(listWord);
	}

	/**
	 * Collect các từ trong <strong>rs</strong> mà chưa có trong
	 * <strong>listAllWord</strong>. Kết hợp tính DF của mỗi từ.
	 * 
	 * @param listAllWord
	 * @param rs
	 * @throws SQLException
	 */
	private void collect(ArrayList<Word> listAllWord, ArrayList<Object> listCmt)
			throws SQLException {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			String segmentCmt = c.getCmt_segment();

			ArrayList<Word> aw = Utils.string2ListWord(segmentCmt);
			for (int i = 0; i < aw.size(); i++) {
				int pos = Utils.indexOf(listAllWord, aw.get(i));
				if (pos == -1) {
					aw.get(i).setDF(1);
					aw.get(i).setId(listAllWord.size() + 1);
					aw.get(i).setCmt_id(c.getId());
					listAllWord.add(aw.get(i));
				} else {
					listAllWord.get(pos).setDF(listAllWord.get(pos).getDF() + 1);
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
	public void write(ArrayList<Object> listCmt, ArrayList<Word> listAllWord, BufferedWriter bw1,
			BufferedWriter bw2) throws SQLException, IOException {
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			String line = "", line1 = "";
			line += c.getId() + " ";
			line += c.getLabel2() + " ";
			line1 += c.getLabel2() + " ";
			ArrayList<Word> words = Utils.string2ListWord(c.getCmt_segment());

			for (int i = 0; i < words.size(); i++) {
				int index = Utils.indexOf(listAllWord, words.get(i));
				words.get(i).setId(listAllWord.get(index).getId());
				words.get(i).setIDF(listAllWord.get(index).getIDF());
				words.get(i).setTF(listAllWord.get(index).getTF());
				// words.get(i).setTFIDF(words.get(i).getIDF() *
				// Math.log10(words.get(i).getTF() + 1));
				// words.get(i).setTFIDF(words.get(i).getIDF() *
				// words.get(i).getTF());
				words.get(i).setTFIDF(words.get(i).getIDF() * words.get(i).getTF());
				words.get(i).setIsStop(listAllWord.get(index).getIsStop());
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

			if (this.context.getTypeOfFrequence().equals("TFIDF")) {
				for (int i = 0; i < words.size(); i++) {
					if (words.get(i).getIsStop() != 1) {
						// line += words.get(i).getId() + ":" +
						// Utils.round(words.get(i).getTFIDF())
						// + " ";
						// line1 += words.get(i).getId() + ":" +
						// Utils.round(words.get(i).getTFIDF())
						// + " ";
						line += words.get(i).getId() + ":" + words.get(i).getTFIDF() + " ";
						line1 += words.get(i).getId() + ":" + words.get(i).getTFIDF() + " ";

					}
				}
			} else if (this.context.getTypeOfFrequence().equals("TF")) {
				for (int i = 0; i < words.size(); i++) {
					if (words.get(i).getIsStop() != 1) {
						line += words.get(i).getId() + ":" + words.get(i).getFrequency() + " ";
						line1 += words.get(i).getId() + ":" + words.get(i).getFrequency() + " ";
					}
				}
			}
			if (line.length() > 2) {
				bw1.write(line);
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
