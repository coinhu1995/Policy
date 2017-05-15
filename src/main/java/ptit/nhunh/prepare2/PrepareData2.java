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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jvntextpro.JVnTextPro;
import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.dao.WordDAO;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;
import vn.hus.nlp.tokenizer.VietTokenizer;

/**
 * Prepare data với chỉ 2 nhãn 1, 3 của label2
 * 
 * @author uhn
 *
 */
public class PrepareData2 {
	private VietTokenizer vietTokenizer;
	private UrlDAO urlDAO;
	private CommentDAO commentDAO;
	private WordDAO wordDAO;

	private BufferedWriter bw;
	private BufferedWriter bw1;
	private BufferedWriter bw2;
	private BufferedWriter bw3;

	public PrepareData2() throws FileNotFoundException {
		// this.textPro = new JVnTextPro();
		// this.textPro.initSegmenter(
		// "D:\\NHU\\WORKSPACE\\Capstone\\JVnTextPro-v.2.0\\models\\jvnsegmenter");
		this.urlDAO = new UrlDAO("Capstone");
		this.commentDAO = new CommentDAO("Capstone");
		this.wordDAO = new WordDAO("Capstone");
		this.vietTokenizer = new VietTokenizer();
		bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\3label\\train"))));
		bw1 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\3label\\test"))));
		bw2 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\3label\\input.train"))));
		bw3 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\3label\\input.test"))));
	}

	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		PrepareData2 prepare = new PrepareData2();
		int train = 800;
		int test = 1085;

		System.out.println("\n--- Start Processing ---");
		long sSentenceSegment = System.currentTimeMillis();
		System.out.println("\t+> Sentence Segmenting...");
		 prepare.sentenceSegment();
		long eSentenceSegment = System.currentTimeMillis();
		System.out.println("\t+> Word Collecting...");
		prepare.collectWord(train);
		long eCollectWord = System.currentTimeMillis();
		System.out.println("\t+> File Generating...");
		prepare.genDataFile(train, test);
		long eGenDataFile = System.currentTimeMillis();
		System.out.println("--- End Processing ---");

		System.out.println(
				"Sentence Segment: " + (eSentenceSegment - sSentenceSegment) / (float) 60000);
		System.out
				.println("Collect Word    : " + (eCollectWord - eSentenceSegment) / (float) 60000);
		System.out.println("Generate File   : " + (eGenDataFile - eCollectWord) / (float) 60000);
	}

	/**
	 * Chuyển các comment thành dạng 1 câu gồm các từ đã được phân đoạn. Và thêm
	 * vào database
	 * 
	 * @param sentence
	 * @return
	 * @throws SQLException
	 */
	private void sentenceSegment() throws SQLException {
		ResultSet rs = commentDAO.getData("select * from TblComment order by id");

		while (rs.next()) {
			String sentence = rs.getString(3);

			sentence = Utils.compound2Unicode(sentence);

			String sentenceSegment = this.sentence2SegmentSentence(sentence);

			commentDAO.updateData("update TblComment set cmt_segment = N'" + sentenceSegment
					+ "' where id = " + rs.getInt(1));
		}
	}

	/**
	 * Chuyển 1 sentence thành 1 sentence đã được phân đoạn.
	 * 
	 * @param sentence
	 * @return
	 */
	private String sentence2SegmentSentence(String sentence) {
		sentence = this.vietTokenizer.tokenize(sentence)[0];

		sentence = Utils.removeSymbol(sentence);
		sentence = sentence.toLowerCase();

		return sentence;
	}

	/**
	 * Collect các từ ở trong limit comments đầu tiên. Và thêm vào Database.
	 * 
	 * @param limit
	 * @throws SQLException
	 * @throws IOException
	 */
	private void collectWord(int limit) throws SQLException, IOException {
		ArrayList<Word> listWord = new ArrayList<>();
		this.wordDAO.updateData("delete from TblWord");
		this.wordDAO.updateData("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
		ResultSet rs = null;
		rs = this.commentDAO
				.getData("select top " + limit + " * from TblComment where label = 1 order by id");

		collect(listWord, rs);

		rs = this.commentDAO
				.getData("select top " + limit + " * from TblComment where label = 2 order by id");

		collect(listWord, rs);

		rs = this.commentDAO
				.getData("select top " + limit + " * from TblComment where label = 3 order by id");

		collect(listWord, rs);

		// Thêm các từ đã tách được từ tập huấn luyện vào CSDL
		// và đánh số thứ tự
		for (int i = 0; i < listWord.size(); i++) {
			try {
				listWord.get(i).setIDF((float) Math.log10((limit) / (1 + listWord.get(i).getDF())));

				this.wordDAO.updateData("insert into TblWord values(N'" + listWord.get(i).getWord()
						+ "', " + listWord.get(i).getDF() + ", " + listWord.get(i).getIDF()
						+ ", 0)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		checkStopWord();
	}

	private void collect(ArrayList<Word> listWord, ResultSet rs) throws SQLException {
		while (rs.next()) {
			String segmentCmt = rs.getString(4);

			ArrayList<Word> aw = Utils.string2ListWord(segmentCmt);
			for (int i = 0; i < aw.size(); i++) {
				int pos = Utils.indexOf(listWord, aw.get(i));

				if (pos == -1) {
					aw.get(i).setDF(1);
					listWord.add(aw.get(i));
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}
	}

	private void checkStopWord() throws IOException, SQLException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("stopword.txt")));
		String s = "";
		while ((s = br.readLine()) != null) {
			ResultSet rs = wordDAO.getData("select * from TblWord where word = N'" + s + "'");
			while (rs.next()) {
				wordDAO.updateData("update TblWord set isStop = 1 where id = " + rs.getInt(1));
			}
		}
		br.close();
	}

	/**
	 * Tạo các file data (input.train, input.test).
	 * 
	 * @param train
	 * @param test
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void genDataFile(int train, int test)
			throws SQLException, IOException, InterruptedException {
		ResultSet rs = this.commentDAO
				.getData("select top " + test + " * from TblComment where label = 1 order by id ");
		write(rs, train);

		rs = this.commentDAO
				.getData("select top " + test + " * from TblComment where label = 2 order by id ");
		write(rs, train);

		rs = this.commentDAO
				.getData("select top " + test + " * from TblComment where label = 3 order by id ");
		write(rs, train);

		bw.close();
		bw1.close();
		bw2.close();
		bw3.close();
	}

	public void write(ResultSet rs, int train) throws SQLException, IOException {
		int dem = 0;
		while (rs.next()) {
			dem++;
			String line = "", line1 = "";
			line += rs.getString(1) + " ";

			line += rs.getString(16) + " ";
			line1 += rs.getString(16) + " ";
			ArrayList<Word> listWord = Utils.string2ListWord(rs.getString(4));

			Collections.sort(listWord, new Comparator<Word>() {
				@Override
				public int compare(Word o1, Word o2) {
					if (o1.getId() > o2.getId())
						return 1;
					if (o1.getId() < o2.getId())
						return -1;
					return 0;
				}
			});

			for (int i = 0; i < listWord.size(); i++) {
				if (listWord.get(i).getId() > 0) {
					if (listWord.get(i).getIsStop() != 1) {
						line += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
						line1 += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
					} else {
						line += listWord.get(i).getId() + ":0 ";
						line1 += listWord.get(i).getId() + ":0 ";
					}
				}
			}
			if (dem <= train && line.length() > 7) {
				bw.write(line);
				bw.newLine();

				bw2.write(line1);
				bw2.newLine();
			} else {
				if (line.length() > 7) {
					bw1.write(line);
					bw1.newLine();

					bw3.write(line1);
					bw3.newLine();
				}
			}
		}
	}
}
