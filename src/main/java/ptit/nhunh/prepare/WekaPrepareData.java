package ptit.nhunh.prepare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;

public class WekaPrepareData {
	private SQLDAO cmtDao;
	private SQLDAO wordDao;

	private ArrayList<Object> listCmt;

	private ArrayList<Word> listWord;
	private String path;
	private String date = LocalDate.now().toString();
	private String time = LocalTime.now().toString();

	public WekaPrepareData() throws SQLException, IOException {
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
		new WekaPrepareData().process();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		System.out.println("--- Start Gen File ---");

		long t1 = System.currentTimeMillis();

		this.listWord = Utils.object2Word(this.wordDao.getAll());
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
		String fileName = this.path + "data.arff";
		String idFileName = this.path + "id.txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));
		BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(idFileName))));

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
			bw.write((c.getLabel2() + "").toCharArray());
			bw.newLine();
			bw1.write(c.getId());
			bw1.newLine();
		}
		bw.close();
		bw1.close();
	}

	private void clear(ArrayList<Word> listWord) {
		for (int i = 0; i < listWord.size(); i++) {
			listWord.get(i).setTimesOccur(0);
		}
	}
}
