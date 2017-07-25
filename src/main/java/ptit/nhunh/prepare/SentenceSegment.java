package ptit.nhunh.prepare;

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
import vn.edu.vnu.uet.nlp.segmenter.UETSegmenter;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class SentenceSegment {
	private static final int TOTAL_CMT = 7888;
	public static final int VNTOKENIZER = 1;
	public static final int UETSEGMENT = 2;

	private VietTokenizer vietTokenizer;
	private UETSegmenter segmenter;
	private SQLDAO cmtDAO;

	private HashMap<String, String> acronymWords = new HashMap<>();
	
	public static void main(String[] args) {
		try {
			new SentenceSegment().process(VNTOKENIZER);
		} catch (FileNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public SentenceSegment() throws FileNotFoundException {
		this.cmtDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.vietTokenizer = new VietTokenizer();
		// this.segmenter = new UETSegmenter("modelsUET");

		Scanner scan = new Scanner(new File("acronyms.txt"));

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			this.acronymWords.put(line.split(":")[0], line.split(":")[1]);
		}
		scan.close();
	}

	/**
	 * <strong>VNTOKENIZER</strong>: to use vntokenizer
	 * <strong>UETSEGMENT</strong>: to use UETsegment
	 * 
	 * @param kernel
	 * @throws SQLException
	 */
	public void process(int kernel) throws SQLException {
		this.sentenceSegment(kernel);
	}

	/**
	 * Chuyển các comment thành dạng 1 câu gồm các từ đã được phân đoạn. Và thêm
	 * vào database
	 * 
	 * @param sentence
	 * @return
	 * @throws SQLException
	 */
	private void sentenceSegment(int kernel) throws SQLException {
		ArrayList<Object> listCmt = this.cmtDAO.getAll();
		int dem = 0;
		int max = 0;
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			dem++;
			String sentence = c.getContent();

			sentence = Utils.compound2Unicode(sentence);
			sentence = sentence.toLowerCase();
			String sentenceSegment = this.sentence2SegmentSentence(sentence, kernel);
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
	 * 
	 * @param sentence
	 * @return
	 */
	public String sentence2SegmentSentence(String sentence, int kernel) {
		sentence = this.replaceAcronymWord(sentence);
		if (kernel == UETSEGMENT) {
			sentence = this.segmenter.segment(sentence);
		}
		if (kernel == VNTOKENIZER) {
			sentence = this.vietTokenizer.tokenize(sentence)[0];
		}
		sentence = Utils.removeSymbol(sentence);
		return sentence;
	}

	public String replaceAcronymWord(String sentence) {
		for (String key : this.acronymWords.keySet()) {
			sentence = sentence.replace(" " + key + " ", " " + this.acronymWords.get(key) + " ");

			if (sentence.indexOf(" ") > 0
					&& sentence.substring(0, sentence.indexOf(" ")).equals(key)) {
				sentence = this.acronymWords.get(key) + sentence.substring(sentence.indexOf(" "));
			}
			if (sentence.indexOf(" ") > 0
					&& sentence.substring(sentence.lastIndexOf(" ") + 1).equals(key)) {
				sentence = sentence.substring(0, sentence.lastIndexOf(" ") + 1)
						+ this.acronymWords.get(key);
			}
		}
		return sentence;
	}
}
