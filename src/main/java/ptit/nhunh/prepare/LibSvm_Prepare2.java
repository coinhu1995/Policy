package ptit.nhunh.prepare;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

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
public class LibSvm_Prepare2 {
	private VietTokenizer vietToken;
	private SQLDAO wordDao;

	private List<Object> listWord;

	public LibSvm_Prepare2() throws SQLException, IOException {
		this.wordDao = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);
		this.vietToken = new VietTokenizer();
		this.listWord = this.wordDao.getAll();
	}

	public static void main(String[] args) throws SQLException, IOException {
		System.out.println(new LibSvm_Prepare2().convert2Vector("tôi không đồng ý"));
	}

	/**
	 * Collect các từ trong <strong>rs</strong> mà chưa có trong
	 * <strong>listAllWord</strong>. Kết hợp tính DF của mỗi từ.
	 * 
	 * @param listWord
	 * @param rs
	 * @throws SQLException
	 * @throws IOException
	 */
	private String convert2Vector(String sentence) throws SQLException, IOException {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);
		sentence = this.vietToken.segment(sentence);
		System.out.println(sentence);
		ArrayList<Word> words = Utils.sentence2Words(sentence);
		this.checkStopWord(words);

		String vector = "1 ";

		for (int i = 0; i < words.size(); i++) {
			if (!words.get(i).isStopWord()) {
				List<Object> list = this.wordDao.getData(
						"select * from TblWord where UPPER(word) like N'%" + words.get(i).getWord().toUpperCase() + "%'");
				if (list.size() > 0) {
					Word w = SerializationUtils.clone((Word) list.get(0));
					w.setTimesOccur(words.get(0).getTimesOccur());
					vector += w.getId() + ":" + w.getTFIDF(7890) + " ";
				}
			}
		}

		return vector;
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

	private void checkAcronymsWord(Object obj) throws IOException, SQLException {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("acronyms.txt"), StandardCharsets.UTF_8));
		String s = "";
		while ((s = br.readLine()) != null) {
			String acronyms = s.substring(0, s.indexOf(":"));
			String replaceWord = s.substring(s.indexOf(":") + 1).trim();
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
		br.close();
	}
}
