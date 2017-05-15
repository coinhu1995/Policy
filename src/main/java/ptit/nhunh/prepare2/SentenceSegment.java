package ptit.nhunh.prepare2;

import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.utils.Utils;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class SentenceSegment {
	private static final int TOTAL_CMT = 7888;
	private VietTokenizer vietTokenizer;
	private CommentDAO commentDAO;
	
	public SentenceSegment() {
		this.commentDAO = new CommentDAO("Capstone");
		this.vietTokenizer = new VietTokenizer();
	}
	
	public void process() throws SQLException{
		sentenceSegment();
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
		int dem = 0;
		int max = 0;
		while (rs.next()) {
			dem++;
			String sentence = rs.getString(3);

			sentence = Utils.compound2Unicode(sentence);
			sentence = sentence.toLowerCase();
			String sentenceSegment = this.sentence2SegmentSentence(sentence);

			commentDAO.updateData("update TblComment set cmt_segment = N'" + sentenceSegment
					+ "' where id = " + rs.getInt(1));

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
	private String sentence2SegmentSentence(String sentence) {
		sentence = this.vietTokenizer.tokenize(sentence)[0];
		sentence = Utils.removeSymbol(sentence);
		return sentence;
	}
}
