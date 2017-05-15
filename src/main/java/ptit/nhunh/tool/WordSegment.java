package ptit.nhunh.tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;

import jvntextpro.JVnTextPro;
import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.dao.WordDAO;
import ptit.nhunh.model.Word;

public class WordSegment {
	private JVnTextPro textPro;
	private UrlDAO urlDAO;
	private CommentDAO commentDAO;
	private WordDAO wordDAO;
	private Collator collator;

	public WordSegment() {
		this.collator = Collator.getInstance();
		this.textPro = new JVnTextPro();
		this.textPro.initSegmenter(
				"D:\\NHU\\WORKSPACE\\Capstone\\JVnTextPro-v.2.0\\models\\jvnsegmenter");
		this.urlDAO = new UrlDAO("Capstone");
		this.commentDAO = new CommentDAO("Capstone");
		this.wordDAO = new WordDAO("Capstone");
	}

	/**
	 * Chuyển đổi một String thành đầu vào của SVM
	 * 
	 * @param sentence
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Word> string2Vector(String sentence) throws SQLException {
		ArrayList<Word> listWord = new ArrayList<>();
		String[] words = this.segment(sentence);
		for (int i = 0; i < words.length; i++) {
			int check = 0;
			for (int j = 0; j < listWord.size(); j++) {
				if (this.collator.equals(listWord.get(j).getWord(), words[i])) {
					listWord.get(j).setTF(listWord.get(j).getTF() + 1);
					check = 1;
					break;
				}
			}
			if (check == 0) {
				Word w = new Word(-1, words[i], 1, 0, 0, 0, 0);
				listWord.add(w);
			}
		}

		for (int i = 0; i < listWord.size(); i++) {
			ResultSet rs = this.wordDAO.getData(
					"select * from TblWord where word = N'" + listWord.get(i).getWord() + "'");
			while (rs.next()) {
				listWord.get(i).setId(rs.getInt(1));
				listWord.get(i).setIDF(rs.getFloat(4));
				listWord.get(i).setTFIDF(
						listWord.get(i).getIDF() * Math.log10(listWord.get(i).getTF() + 1));
				listWord.get(i).setIsStop(rs.getInt(5));
				break;
			}
		}

		return listWord;
	}

	/**
	 * Phân tách một câu thành danh sách các từ. Theo JVNTextPro
	 * 
	 * @param sentence
	 * @return
	 */
	public String[] segment(String sentence) {
		String s = this.textPro.wordSegment(sentence);
		s = removeSymbol(s);
		while (s.indexOf("  ") >= 0) {
			s = s.replace("  ", " ");
		}
		String[] listWord = s.split(" ");
		for (int i = 0; i < listWord.length; i++) {
			listWord[i] = listWord[i].replace("_", " ");
			listWord[i] = listWord[i].trim();
		}
		return listWord;
	}

	public static String removeSymbol(String sentence) {
		for (int i = 33; i <= 47; i++) {
			sentence = sentence.replace(String.valueOf((char) i), " ");
		}
		for (int i = 58; i <= 62; i++) {
			sentence = sentence.replace(String.valueOf((char) i), " ");
		}
		return sentence;
	}
}
