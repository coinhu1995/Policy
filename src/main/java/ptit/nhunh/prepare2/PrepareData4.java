package ptit.nhunh.prepare2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang3.SerializationUtils;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.WordDAO;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 * Prepare data với chỉ 2 nhãn 1, 3 IDF của data test tính gộp cả training.
 * 
 * @author uhn
 *
 */

public class PrepareData4 {
	private VietTokenizer vietToken;
	private CommentDAO commentDAO;
	private WordDAO wordDAO;

	private BufferedWriter bw;
	private BufferedWriter bw1;
	private BufferedWriter bw2;
	private BufferedWriter bw3;

	private ArrayList<Word> wordsOfTrainingData;
	private ArrayList<Word> wordsOfTrainingTestingData;

	private int train = 4500;
	private int test = 5089;

	public PrepareData4() throws SQLException, IOException {
		this.commentDAO = new CommentDAO("Capstone");
		this.wordDAO = new WordDAO("Capstone");
		this.vietToken = new VietTokenizer();
		String path = "src\\main\\resource\\data\\2label\\"
				+ LocalDate.now().toString().replaceAll("-", "") + "\\"
				+ LocalTime.now().toString().substring(0, 8).replace(":", "");
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

		this.wordDAO.updateData("delete from TblWord");
		this.wordDAO.updateData("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
	}

	public static void main(String[] args)
			throws FileNotFoundException, SQLException, IOException, InterruptedException {
		new PrepareData4().process();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		PrepareData4 prepare = new PrepareData4();

		System.out.println("\n--- Start Processing ---");

		long sSentenceSegment = System.currentTimeMillis();
		// prepare.sentenceSegment();
		long eSentenceSegment = System.currentTimeMillis();

		prepare.genTrainingDataFile(this.train);
		long eGenTrainDataFile = System.currentTimeMillis();

		prepare.genTestingDataFile(this.train, this.test);
		long eGenTestDataFile = System.currentTimeMillis();

		System.out.println("--- End Processing ---");

		this.close();

		System.out.println(
				"Sentence Segment: " + (eSentenceSegment - sSentenceSegment) / (float) 60000);
		System.out.println("Generate Training File   : "
				+ (eGenTrainDataFile - eSentenceSegment) / (float) 60000);
		System.out.println("Generate Testing File   : "
				+ (eGenTestDataFile - eGenTrainDataFile) / (float) 60000);
	}

	/**
	 * Chuyển các comment thành dạng 1 câu gồm các từ đã được phân đoạn. Và thêm
	 * vào database
	 * 
	 * @param sentence
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private void sentenceSegment() throws SQLException {
		System.out.println("\t+> Sentence Segmenting...");
		ResultSet rs = this.commentDAO.getData("select * from TblComment order by id");

		while (rs.next()) {
			String sentence = rs.getString(3);
			sentence = Utils.compound2Unicode(sentence);
			String sentenceSegment = this.sentence2SegmentSentence(sentence);
			this.commentDAO.updateData("update TblComment set cmt_segment = N'" + sentenceSegment
					+ "' where id = " + rs.getInt(1));
		}
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
		ResultSet rs = this.commentDAO
				.getData("select * from TblComment where id <= " + train + " order by id ");

		this.write(rs, this.wordsOfTrainingData, this.bw, this.bw2);
	}

	private void genTestingDataFile(int train, int test)
			throws SQLException, IOException, InterruptedException {
		System.out.println("\t+> Testing file Generating...");

		ResultSet rs = this.commentDAO.getData("select * from TblComment where id > " + train
				+ " and id <= " + test + " order by id ");

		this.wordsOfTrainingTestingData = SerializationUtils.clone(this.wordsOfTrainingData);

		this.collect(this.wordsOfTrainingTestingData, rs);

		for (int i = 0; i < this.wordsOfTrainingTestingData.size(); i++) {
			this.wordsOfTrainingTestingData.get(i).setIDF((float) Math
					.log10((test) / (1 + this.wordsOfTrainingTestingData.get(i).getDF())));
		}

		rs = this.commentDAO.getData("select * from TblComment where id > " + train + " and id <= "
				+ test + " order by id ");
		this.write(rs, this.wordsOfTrainingTestingData, this.bw1, this.bw3);
	}

	/**
	 * Chuyển 1 sentence thành 1 sentence đã được phân đoạn.
	 * 
	 * @param sentence
	 * @return
	 */
	private String sentence2SegmentSentence(String sentence) {
		sentence = this.vietToken.tokenize(sentence)[0];
		sentence = Utils.removeSymbol(sentence);
		sentence = sentence.toLowerCase();
		return sentence;
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

		ResultSet rs = null;
		rs = this.commentDAO
				.getData("select * from TblComment where id <= " + train + " order by id");

		this.collect(listWord, rs);

		// Thêm các từ đã tách được từ tập huấn luyện vào CSDL
		for (int i = 0; i < listWord.size(); i++) {
			try {
				listWord.get(i).setIDF((float) Math.log10((train) / (1 + listWord.get(i).getDF())));

				this.wordDAO.updateData("insert into TblWord values(N'" + listWord.get(i).getWord()
						+ "', " + listWord.get(i).getDF() + ", " + listWord.get(i).getIDF()
						+ ", 0)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.wordsOfTrainingData = SerializationUtils.clone(listWord);
		this.checkStopWord();
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
	public void write(ResultSet rs, ArrayList<Word> listAllWord, BufferedWriter bw1,
			BufferedWriter bw2) throws SQLException, IOException {
		while (rs.next()) {
			String line = "", line1 = "";
			line += rs.getString(1) + " ";

			line += rs.getString(17) + " ";
			line1 += rs.getString(17) + " ";
			ArrayList<Word> words = Utils.string2ListWord(rs.getString(4));

			for (int i = 0; i < words.size(); i++) {
				int index = Utils.indexOf(listAllWord, words.get(i));
				words.get(i).setId(listAllWord.get(index).getId());
				words.get(i).setIDF(listAllWord.get(index).getIDF());
				words.get(i).setTFIDF(words.get(i).getIDF() * Math.log10(words.get(i).getTF() + 1));
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

			for (int i = 0; i < words.size(); i++) {
				if (words.get(i).getIsStop() != 1) {
					line += words.get(i).getId() + ":" + words.get(i).getTFIDF() + " ";
					line1 += words.get(i).getId() + ":" + words.get(i).getTFIDF() + " ";
				} 
			}
			bw1.write(line);
			bw1.newLine();
			bw2.write(line1);
			bw2.newLine();
		}

		bw1.close();
		bw2.close();
	}

//	public void write2(ResultSet rs, ArrayList<Word> listAllWord, BufferedWriter bw1,
//			BufferedWriter bw2) throws SQLException, IOException {
//		while (rs.next()) {
//			ArrayList<Word> law = SerializationUtils.clone(listAllWord);
//
//			String line = "", line1 = "";
//			line += rs.getString(1) + " ";
//
//			line += rs.getString(17) + " ";
//			line1 += rs.getString(17) + " ";
//			ArrayList<Word> words = Utils.string2ListWord(rs.getString(4));
//
//			for (int i = 0; i < words.size(); i++) {
//				int index = Utils.indexOf(listAllWord, words.get(i));
//
//				law.get(index).setTF(words.get(i).getTF());
//				law.get(index)
//						.setTFIDF(law.get(index).getIDF() * Math.log10(law.get(index).getTF() + 1));
//			}
//
//			for (int i = 0; i < law.size(); i++) {
//				if (law.get(i).getIsStop() != 1) {
//					line += law.get(i).getId() + ":" + law.get(i).getTFIDF() + " ";
//					line1 += law.get(i).getId() + ":" + law.get(i).getTFIDF() + " ";
//				} else {
//					line += law.get(i).getId() + ":0 ";
//					line1 += law.get(i).getId() + ":0 ";
//				}
//			}
//			bw1.write(line);
//			bw1.newLine();
//			bw2.write(line1);
//			bw2.newLine();
//			System.out.println("\t\t" + rs.getString(1));
//		}
//
//		bw1.close();
//		bw2.close();
//	}
//
//	/**
//	 * for weka
//	 * 
//	 * @param rs
//	 * @param listAllWord
//	 * @param bw1
//	 * @param bw2
//	 * @throws SQLException
//	 * @throws IOException
//	 */
//	public void write3(ResultSet rs, ArrayList<Word> listAllWord, BufferedWriter bw1,
//			BufferedWriter bw2) throws SQLException, IOException {
//		while (rs.next()) {
//			ArrayList<Word> law = SerializationUtils.clone(listAllWord);
//
//			String line = "", line1 = "";
//			line += rs.getString(1) + " ";
//
//			ArrayList<Word> words = Utils.string2ListWord(rs.getString(4));
//
//			for (int i = 0; i < words.size(); i++) {
//				int index = Utils.indexOf(listAllWord, words.get(i));
//
//				law.get(index).setTF(words.get(i).getTF());
//				law.get(index)
//						.setTFIDF(law.get(index).getIDF() * Math.log10(law.get(index).getTF() + 1));
//			}
//
//			for (int i = 0; i < law.size(); i++) {
//				if (law.get(i).getIsStop() != 1) {
//					line += (int) (law.get(i).getTFIDF() * 100) / (double) 100;
//					line1 += (int) (law.get(i).getTFIDF() * 100) / (double) 100;
//					line += ",";
//					line1 += ",";
//				} else {
//					line += "0";
//					line1 += "0";
//					line += ",";
//					line1 += ",";
//				}
//			}
//
//			line += rs.getString(17);
//			line1 += rs.getString(17);
//
//			bw1.write(line);
//			bw1.newLine();
//			bw2.write(line1);
//			bw2.newLine();
//			System.out.println("\t\t" + rs.getString(1));
//		}
//
//		bw1.close();
//		bw2.close();
//	}

	/**
	 * Collect các từ trong <strong>rs</strong> mà chưa có trong
	 * <strong>listAllWord</strong>. Kết hợp tính DF của mỗi từ.
	 * 
	 * @param listAllWord
	 * @param rs
	 * @throws SQLException
	 */
	private void collect(ArrayList<Word> listAllWord, ResultSet rs) throws SQLException {
		Collator c = Collator.getInstance();
		c.setStrength(Collator.TERTIARY);
		while (rs.next()) {
			String segmentCmt = rs.getString(4);

			ArrayList<Word> aw = Utils.string2ListWord(segmentCmt);
			for (int i = 0; i < aw.size(); i++) {
				int pos = Utils.indexOf(listAllWord, aw.get(i));
				if (pos == -1) {
					aw.get(i).setDF(1);
					aw.get(i).setId(listAllWord.size() + 1);
					listAllWord.add(aw.get(i));
				} else {
					listAllWord.get(pos).setDF(listAllWord.get(pos).getDF() + 1);
				}
			}
		}
	}

	private void checkStopWord() throws IOException, SQLException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("stopword.txt")));
		String s = "";
		while ((s = br.readLine()) != null) {
			ResultSet rs = this.wordDAO.getData("select * from TblWord where word = N'" + s + "'");
			while (rs.next()) {
				this.wordDAO.updateData("update TblWord set isStop = 1 where id = " + rs.getInt(1));
				this.wordsOfTrainingData.get(rs.getInt(1)).setIsStop(1);
			}
		}
		br.close();
	}

	private void close() throws IOException {
		this.bw.flush();
		this.bw1.flush();
		this.bw2.flush();
		this.bw3.flush();
		this.bw.close();
		this.bw1.close();
		this.bw2.close();
		this.bw3.close();
	}
}
