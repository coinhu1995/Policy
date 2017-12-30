package ptit.nhunh.prepare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;

public class WekaPrepareData2 {
	private SQLDAO cmtDao;
	private SQLDAO wordDao;

	private ArrayList<Object> listTrainCmt;

	private ArrayList<Word> listWord;
	private String path;
	public WekaPrepareData2() throws SQLException, IOException {
		this.cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.wordDao = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);
		this.listWord = new ArrayList<>();

		String date = LocalDate.now().toString();
		String time = LocalTime.now().toString();

		this.path = "src\\main\\resources\\data\\" + date.replaceAll("-", "") + "\\"
				+ time.substring(0, 5).replace(":", "");
		BufferedWriter pathWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("path.txt"))));
		pathWriter.write(this.path);
		pathWriter.close();

		File folder = new File(this.path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		this.listTrainCmt = this.cmtDao.getData("select * from TblComment where id <= 1164 order by id");
		this.checkAcronymsWord(this.listTrainCmt);
		this.wordDao.update("delete from TblWord");
		this.wordDao.update("DBCC CHECKIDENT ('TblWord', RESEED, 0)");
	}

	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		new WekaPrepareData2().process();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		System.out.println("--- Start Processing ---");

		long t1 = System.currentTimeMillis();

		this.genTrainingDataFile();

		long t2 = System.currentTimeMillis();

		System.out.println("Generate Training File   : " + (t2 - t1) / (float) 60000);
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
	private void genTrainingDataFile() throws SQLException, IOException, InterruptedException {
		System.out.println("\t+> Training file Generating...");

		this.collect(this.listWord, this.listTrainCmt);
		this.checkStopWord(this.listWord);
		this.write(this.listTrainCmt);
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
					listWord.add(aw.get(i));
				} else {
					listWord.get(pos).setDF(listWord.get(pos).getDF() + 1);
				}
			}
		}
		
		for (int i = 0; i < listWord.size(); i++) {
			this.wordDao.insert(listWord.get(i));
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
	public void write(ArrayList<Object> listCmt) throws SQLException, IOException {
		this.clear(this.listWord);
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("arff" + listCmt.size() + ".arff"))));

		bw.write("@relation policy\n");
		for (int i = 0; i < this.listWord.size(); i++) {
			bw.write("@attribute " + (i + 1) + " NUMERIC\n");
		}
		bw.write("@attribute label {1,3}\n\n");
		bw.write("@data\n");

		for (Object o : listCmt) {
			Comment c = (Comment) o;
			ArrayList<Word> words = Utils.sentence2Words(c.getCmt_segment());
			ArrayList<Word> listAll = SerializationUtils.clone(this.listWord);

			for (int i = 0; i < words.size(); i++) {
				int index = Utils.indexOf(listAll, words.get(i));
				if (index != -1 && !listAll.get(index).isStopWord()) {
					listAll.get(index).setTimesOccur(words.get(i).getTimesOccur());
				}
			}

			for (int i = 0; i < listAll.size(); i++) {
				bw.write(String.valueOf(listAll.get(i).getTFIDF(listCmt.size())).substring(0, 3) + ",");
			}
			bw.write((c.getLabel() + "").toCharArray());
			bw.newLine();
		}
		bw.close();
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

	public void clear(ArrayList<Word> listWord) {
		for (int i = 0; i < listWord.size(); i++) {
			listWord.get(i).setTimesOccur(0);
		}
	}
}
