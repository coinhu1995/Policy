package ptit.nhunh.segment;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.utils.Utils;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class SentenceSegment {
	private static final int TOTAL_CMT = 7888;

	private VietTokenizer vietTokenizer;
	private SQLDAO cmtDAO;

	private HashMap<String, String> acronymWords = new HashMap<>();

	public SentenceSegment() throws FileNotFoundException {
		this.cmtDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.vietTokenizer = new VietTokenizer();

		Scanner scan = new Scanner(new File("acronyms.txt"));

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			this.acronymWords.put(line.split(":")[0], line.split(":")[1]);
		}
		scan.close();
	}

	public void process() throws SQLException {
		this.sentenceSegment();
	}

	/**
	 * Chuyển các comment thành dạng 1 câu gồm các từ đã được phân đoạn. Và thêm vào
	 * database
	 */
	private void sentenceSegment() throws SQLException {
		ArrayList<Object> listCmt = this.cmtDAO.getAll();
		int dem = 0;
		int max = 0;
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			dem++;
			String sentence = c.getContent();

			sentence = Utils.compound2Unicode(sentence);
			sentence = sentence.toLowerCase();
			String sentenceSegment = this.sentence2SegmentSentence(sentence);
			c.setCmt_segment(sentenceSegment);
			this.cmtDAO.update(c, CommentDAO.UPDATE_COMMENT_SEGMENT);

			if ((int) (dem / (float) TOTAL_CMT * (100)) > max) {
				max = (int) (dem / (float) TOTAL_CMT * (100));
				System.out.println("\t\t Percent: " + max + "%");
			}
		}
	}

	/**
	 * Chuyển 1 sentence thành 1 sentence đã được phân đoạn.
	 */
	public String sentence2SegmentSentence(String sentence) {
		sentence = this.replaceAcronymWord(sentence);
		sentence = this.vietTokenizer.tokenize(sentence)[0];
		sentence = Utils.removeSymbol(sentence);
		return sentence;
	}

	/**
	 * thay thế các từ viết tắt hoặc từ lóng.
	 * 
	 * @param sentence
	 *            câu cần loại bỏ các từ viết tắt, từ lóng.
	 * @return câu đã được thay thế các từ viết tắt, từ lóng.
	 */
	public String replaceAcronymWord(String sentence) {
		for (String key : this.acronymWords.keySet()) {
			sentence = sentence.replace(" " + key + " ", " " + this.acronymWords.get(key) + " ");

			if (sentence.indexOf(" ") > 0 && sentence.substring(0, sentence.indexOf(" ")).equals(key)) {
				sentence = this.acronymWords.get(key) + sentence.substring(sentence.indexOf(" "));
			}
			if (sentence.indexOf(" ") > 0 && sentence.substring(sentence.lastIndexOf(" ") + 1).equals(key)) {
				sentence = sentence.substring(0, sentence.lastIndexOf(" ") + 1) + this.acronymWords.get(key);
			}
		}
		return sentence;
	}
}
