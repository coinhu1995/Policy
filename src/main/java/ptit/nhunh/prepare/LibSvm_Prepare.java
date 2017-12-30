package ptit.nhunh.prepare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.SerializationUtils;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;

/**
 * Prepare data với chỉ 2 nhãn 1, 3 IDF của data test tính gộp cả training. Su
 * dung vnTokenizer
 * 
 * @author uhn
 *
 */
public class LibSvm_Prepare {
	private SQLDAO cmtDao;
	private SQLDAO wordDao;

	private ArrayList<Object> listCmt;

	private ArrayList<Word> listWord;
	private String path;
	private String date = LocalDate.now().toString();
	private String time = LocalTime.now().toString();

	public LibSvm_Prepare() throws SQLException, IOException {
		this.cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.wordDao = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);
		this.listWord = new ArrayList<>();

		this.listCmt = this.cmtDao.getData("select * from TblComment where label = 0 order by id");

		this.path = "src\\main\\resources\\data\\" + this.date.replaceAll("-", "") + "\\"
				+ this.time.substring(0, 5).replace(":", "") + "\\";

		BufferedWriter pathWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("src\\main\\resources\\path.txt"))));
		pathWriter.write(this.path);
		pathWriter.close();

		File folder = new File(this.path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		new LibSvm_Prepare().process();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		System.out.println("--- Start Gen File ---");

		long t1 = System.currentTimeMillis();

		this.listWord = (ArrayList<Word>) this.read();
		this.write(this.listCmt);

		long t2 = System.currentTimeMillis();

		System.out.println("Generate Training File   : " + (t2 - t1) / (float) 60000);
		System.out.println("--- End Gen File ---");
	}

	/**
	 * Chuyển các câu ở trong rs thành dạng vector và ghi ra file
	 */
	public void write(ArrayList<Object> listCmt) throws SQLException, IOException {
		this.clear(this.listWord);
		String fileName = this.path + "data.txt";
		String fileId = this.path + "id.txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileId))));
		for (Object o : listCmt) {

			Comment c = (Comment) o;
			bw.write(("-1 ").toCharArray());
			ArrayList<Word> words = Utils.sentence2Words(c.getCmt_segment());
			ArrayList<Word> listAll = SerializationUtils.clone(this.listWord);

			for (int i = 0; i < words.size(); i++) {
				int index = Utils.indexOf(listAll, words.get(i));
				if (index != -1 && !listAll.get(index).isStopWord()) {
					listAll.get(index).setTimesOccur(words.get(i).getTimesOccur());
				}
			}

			for (int i = 0; i < listAll.size(); i++) {
				bw.write(listAll.get(i).getId() + ":"
						+ String.valueOf(listAll.get(i).getTFIDF(listCmt.size())).substring(0, 3) + " ");
			}
			bw2.write((c.getId() + "").toCharArray());
			bw2.newLine();
			bw.newLine();
		}
		bw2.close();
		bw.close();
	}

	private void clear(ArrayList<Word> listWord) {
		for (int i = 0; i < listWord.size(); i++) {
			listWord.get(i).setTimesOccur(0);
		}
	}

	private List<Word> read() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("word.txt"));
		List<Word> listWord = new ArrayList<>();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			Word w = new Word();
			w.setId(Integer.parseInt(line.substring(0, 10).trim()));
			w.setWord(line.substring(10, 110).trim());
			w.setDF(Integer.parseInt(line.substring(110, 120).trim()));
			w.setStopWord(line.substring(120).trim().equals("1") ? true : false);
			listWord.add(w);
		}
		scan.close();
		return listWord;
	}

}
