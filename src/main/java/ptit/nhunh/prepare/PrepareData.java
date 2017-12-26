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
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;

import ptit.nhunh.classification.Classifier;
import ptit.nhunh.context.Context;
import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Constants;
import ptit.nhunh.utils.Utils;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 * Prepare data với chỉ 2 nhãn, IDF của data test tính gộp cả training. Su dung
 * vnTokenizer
 * 
 * @author uhn
 *
 */
@SuppressWarnings("unused")
public class PrepareData {
	private VietTokenizer vietToken;
	private SQLDAO cmtTestDao;
	private SQLDAO cmtDao;
	private SQLDAO wordDao;

	private BufferedWriter trainWriter;
	private BufferedWriter testWriter;
	private BufferedWriter trainDetailWriter;
	private BufferedWriter testDetailWriter;

	private ArrayList<Object> listTrainCmt;
	private ArrayList<Object> listTestCmt;

	private ArrayList<Word> listWordForTrain;
	private ArrayList<Word> listWordForTest;

	private int labelCount = 2;

	public PrepareData() throws SQLException, IOException {
		this.cmtTestDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
		this.cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.wordDao = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);

		// this.vietToken = new VietTokenizer();

		this.listTestCmt = new ArrayList<>();
		this.listTrainCmt = new ArrayList<>();
		// this.listTrainCmt = this.cmtTestDao
		// .getData("select * from TblCommentTest where id <= " + Context.TRAINSIZE + "
		// order by id");
		// this.listTestCmt = this.cmtTestDao.getData("select * from TblCommentTest
		// where id <= "
		// + (Context.TESTSIZE + Context.TRAINSIZE) + " and id > " + Context.TRAINSIZE +
		// " order by id");
		// this.listTestCmt = this.cmtDao.getData("select top 100 * from TblComment");

		this.readFromDB();

		this.listWordForTrain = new ArrayList<>();

		this.openFile();

		// this.read();
		this.checkAcronymsWord(this.listTestCmt);
		this.checkAcronymsWord(this.listTrainCmt);

		this.wordDao.update("delete from TblWord");
		this.wordDao.update("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
	}

	public static void main(String[] args)
			throws FileNotFoundException, SQLException, IOException, InterruptedException, ClassNotFoundException {
		new PrepareData().process();
		new Classifier().execute();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		// Collect tập từ trong list training và testing.
		System.out.println("--- Collecting word ---");
		this.collect(this.listWordForTrain, this.listTrainCmt);
		this.listWordForTest = SerializationUtils.clone(this.listWordForTrain);
		this.collect(this.listWordForTest, this.listTestCmt);

		for (int i = this.listWordForTrain.size(); i < this.listWordForTest.size(); i++) {
			Word word = SerializationUtils.clone(this.listWordForTest.get(i));
			word.setDF(0);
			this.listWordForTrain.add(word);
		}

		this.checkStopWord(this.listWordForTrain);
		this.checkStopWord(this.listWordForTest);

		System.out.println("--- Start Processing ---");

		this.genTrainingDataFile(Context.TRAINSIZE);
		this.genTestingDataFile(Context.TRAINSIZE, Context.TESTSIZE + Context.TRAINSIZE);

		System.out.println("--- End Processing ---");
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
	private void genTrainingDataFile(int train) throws SQLException, IOException, InterruptedException {
		System.out.println("\t+> Training file Generating...");
		this.write(this.listTrainCmt, this.listWordForTrain, this.trainWriter, this.trainDetailWriter,
				Context.TRAINSIZE);
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
	private void genTestingDataFile(int train, int test) throws SQLException, IOException, InterruptedException {
		System.out.println("\t+> Testing file Generating...");
		this.write(this.listTestCmt, this.listWordForTest, this.testWriter, this.testDetailWriter,
				Context.TRAINSIZE + Context.TESTSIZE);
	}

	/**
	 * Collect các từ trong <strong>listCmt</strong> mà chưa có trong
	 * <strong>listWord</strong>. Kết hợp tính DF của mỗi từ.
	 * 
	 * @param listWord
	 * @param rs
	 * @throws SQLException
	 */
	private void collect(ArrayList<Word> listWord, ArrayList<Object> listCmt) throws SQLException {
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			String segmentCmt = c.getCmt_segment();

			ArrayList<Word> aw = Utils.sentence2Words(segmentCmt);
			for (int i = 0; i < aw.size(); i++) {
				int pos = Utils.indexOf(listWord, aw.get(i));
				if (pos == -1) {
					aw.get(i).setId(listWord.size() + 1);
					aw.get(i).setDF(1);
					aw.get(i).setTimesOccur(0);
					listWord.add(aw.get(i));
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}
		
		for(Word w : listWord) {
			this.wordDao.insert(w);
		}
	}

	/**
	 * <p>
	 * Chuyển các câu ở trong <strong>listCmt</strong> thành dạng vector và ghi ra
	 * file <strong>detailWriter</strong>, <strong>writer</strong>.
	 * </p>
	 * 
	 * <strong>bw1</strong>: các câu có id ở đầu để map vào Database <br>
	 * <strong>bw2</strong>: các câu không có id nhưng map các dòng với bw1.
	 * 
	 * @param rs
	 * @param writer
	 *            chứa câu comment dạng vector
	 * @param detailWriter
	 *            giống param writer nhưng thêm id của câu comment để mapping
	 * @throws SQLException
	 * @throws IOException
	 */
	public void write(ArrayList<Object> listCmt, ArrayList<Word> listWord, BufferedWriter detailWriter,
			BufferedWriter writer, int totalDocument) throws SQLException, IOException {
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("arff" + listCmt.size() + ".arff"))));
		bw.write("@relation " + "arff" + listCmt.size() + "\n");
		for (int i = 0; i < listWord.size(); i++) {
			bw.write("@attribute " + (i + 1) + " NUMERIC\n");
		}
		bw.write("@attribute label {1,2}\n\n");
		bw.write("@data\n");

		for (Object o : listCmt) {
			Comment c = (Comment) o;

			String detailLine = "";
			String tfidfLine = "";

			detailLine += c.getId() + " ";
			detailLine += c.getLabel() + " ";
			tfidfLine += c.getLabel() + " ";

			ArrayList<Word> vector = SerializationUtils.clone(listWord);

			ArrayList<Word> words = Utils.sentence2Words(c.getCmt_segment());

			for (Word word : words) {
				int index = Utils.indexOf(vector, word);
				if (index > -1) {
					vector.get(index).setTimesOccur(word.getTimesOccur());
				}
			}

			for (int i = 0; i < vector.size(); i++) {
				Word word = vector.get(i);
				if (!word.isStopWord()) {
					if (word.getDF() != 0) {
						detailLine += word.getWord() + ":" + word.getTimesOccur() + ":" + word.getDF() + ":"
								+ word.getTFIDF(totalDocument) + " ";
						tfidfLine += word.getId() + ":"
								+ String.valueOf(word.getTFIDF(totalDocument)).substring(0, 3) + " ";
						// bw.write(String.valueOf(word.getTFIDF(totalDocument, 0)).substring(0, 3));
					} else {
						// bw.write("0");
					}
				} else {
					// bw.write("0");
				}
				// bw.write(",");
			}
			// bw.write(c.getLabel() + "");
			// bw.newLine();

			if (detailLine.length() > 2) {
				detailWriter.write(detailLine);
				detailWriter.write("\t\t" + c.getCmt_segment());
				detailWriter.newLine();
				writer.write(tfidfLine);
				writer.newLine();
			}

			System.out.println(c.getId() + " done");
		}
		bw.close();
		detailWriter.close();
		writer.close();

	}

	private void checkStopWord(ArrayList<Word> listWord) throws IOException, SQLException {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("stopword.txt"), StandardCharsets.UTF_8));
		String s = "";
		while ((s = br.readLine()) != null) {
			for (Word word : listWord) {
				if (collator.equals(s, word.getWord())) {
					word.setStopWord(true);
				}
			}
		}
		br.close();
	}

	private void checkAcronymsWord(ArrayList<Object> listCmt) throws IOException, SQLException {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("acronyms.txt"), StandardCharsets.UTF_8));
		String s = "";
		while ((s = br.readLine()) != null) {
			String acronyms = s.substring(0, s.indexOf(":"));
			String replaceWord = s.substring(s.indexOf(":") + 1).trim();
			for (Object obj : listCmt) {
				Comment cmt = (Comment) obj;
				String word = " " + acronyms + " ";
				if (cmt.getCmt_segment().length() > acronyms.length()) {
					if (cmt.getCmt_segment().indexOf(word) >= 0) {
						cmt.setCmt_segment(cmt.getCmt_segment().replace(word, " " + replaceWord + " "));
					}
					if (cmt.getCmt_segment().substring(0, acronyms.length() + 1).equals(acronyms + " ")) {
						cmt.setCmt_segment(replaceWord + cmt.getCmt_segment().substring(acronyms.length()));
					}
					if (cmt.getCmt_segment().substring(cmt.getCmt_segment().length() - acronyms.length() - 1).trim()
							.equals(" " + acronyms)) {
						cmt.setCmt_segment(
								cmt.getCmt_segment().substring(0, cmt.getCmt_segment().length() - acronyms.length())
										+ replaceWord);
					}
				}
			}
		}
		br.close();
	}

	private void read() throws NumberFormatException, IOException {
		BufferedReader br1 = null, br2 = null;
		if (Context.TYPEOFCOPYDATA2DATABASE == 1) {
			br1 = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("src\\main\\resources\\data\\100\\1_150.txt")),
							StandardCharsets.UTF_8));
			br2 = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("src\\main\\resources\\data\\100\\2_150.txt")),
							StandardCharsets.UTF_8));
		} else if (Context.TYPEOFCOPYDATA2DATABASE == 2) {
			br1 = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("src\\main\\resources\\data\\100\\1_100.txt")),
							StandardCharsets.UTF_8));
			br2 = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("src\\main\\resources\\data\\100\\2_100.txt")),
							StandardCharsets.UTF_8));
		}
		ArrayList<Comment> label1 = new ArrayList<>();
		ArrayList<Comment> label2 = new ArrayList<>();
		String line1 = "", line2 = "";
		while ((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
			Comment c1 = new Comment();
			c1.setLabel(Integer.parseInt(line1.substring(0, 1)));
			c1.setId(Integer.parseInt(line1.substring(2, 13).trim()));
			c1.setCmt_segment(line1.substring(14).trim());
			label1.add(c1);
			Comment c2 = new Comment();
			c2.setLabel(Integer.parseInt(line2.substring(0, 1)));
			c2.setId(Integer.parseInt(line2.substring(2, 13).trim()));
			c2.setCmt_segment(line2.substring(14).trim());
			label2.add(c2);
		}
		for (int i = 0; i < (Context.TRAINSIZE + Context.TESTSIZE) / 2; i++) {
			if (i < Context.TRAINSIZE / 2) {
				this.listTrainCmt.add(label1.get(i));
			} else {
				this.listTestCmt.add(label1.get(i));
			}
		}
		for (int i = 0; i < (Context.TRAINSIZE + Context.TESTSIZE) / 2; i++) {
			if (i < Context.TRAINSIZE / 2) {
				this.listTrainCmt.add(label2.get(i));
			} else {
				this.listTestCmt.add(label2.get(i));
			}
		}
	}

	private void openFile() throws IOException {
		String path = Constants.DATA_PATH + this.labelCount + "_label\\" + Utils.getCurrentDate() + "\\"
				+ Utils.getCurrentTime();

		BufferedWriter pathWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("path.txt"))));
		pathWriter.write(path);
		pathWriter.close();

		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		String train = "", test = "", detailTrain = "", detailTest = "";

		if (Context.TYPEOFFILE == 1) {
			detailTrain = "train";
			detailTest = "test";
			train = "input.train";
			test = "input.test";
		} else if (Context.TYPEOFFILE == 2) {
			detailTrain = "train";
			detailTest = "test";
			train = "input.train";
			test = "input.test";
		}

		this.trainWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\train")), StandardCharsets.UTF_8));
		this.testWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\test")), StandardCharsets.UTF_8));
		this.trainDetailWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\input.train")), StandardCharsets.UTF_8));
		this.testDetailWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(path + "\\input.test")), StandardCharsets.UTF_8));
	}

	public void readFromDB() throws SQLException {
		this.listTrainCmt = this.cmtDao.getData("select * from TblComment where id <= 7890 order by id");

		this.listTestCmt = this.cmtDao.getData("select * from TblComment where id > 7890 order by id");
	}
}
