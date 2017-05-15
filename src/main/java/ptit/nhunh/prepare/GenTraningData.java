package ptit.nhunh.prepare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.dao.WordDAO;
import ptit.nhunh.model.Word;
import ptit.nhunh.tool.CheckStopWord;
import ptit.nhunh.tool.WordSegment;

/**
 * 
 * Đọc tất cả comment trong DB rồi convert sang dạng file InputSVM
 * 
 * @throws FileNotFoundException
 * @throws SQLException
 * @throws IOException
 * 
 * @author uhn
 *
 */
public class GenTraningData {
	private BufferedWriter bw;
	private BufferedWriter bw1;
	private BufferedWriter bw2;
	private BufferedWriter bw3;
	private UrlDAO urlDAO;
	private CommentDAO commentDAO;
	private WordDAO wordDAO;
	private WordSegment wordSegment;

	public static void main(String[] args)
			throws FileNotFoundException, SQLException, IOException, InterruptedException {
		new GenTraningData().process();
	}

	public GenTraningData() throws FileNotFoundException, SQLException {
		this.bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("train"))));
		this.bw1 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("test"))));
		this.bw2 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("input.train"))));
		this.bw3 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("input.test"))));
		this.urlDAO = new UrlDAO("Capstone");
		this.commentDAO = new CommentDAO("Capstone");
		this.wordDAO = new WordDAO("Capstone");
		
		System.out.println("\n-------------Prepare for word segment--------------");
		this.wordSegment = new WordSegment();
		System.out.println("-------------Done prepare word segment--------------");
		System.out.println();
	}

	public void process() throws SQLException, IOException, InterruptedException {
		int train = 4000;
		int test = 4500;

		new CollectWord(wordSegment).process(train);

		new CheckStopWord().check();
		
		genTraningData(train, test);
		// long sCollect = System.currentTimeMillis();
		// new CollectWord(this.wordSegment).collectWord(0, train);
		// long eCollect = System.currentTimeMillis();
		//
		// System.out.println("\n\n\n-------------Starting generate Training
		// File--------------");
		// genTraningData(train, test);
		//
		// long eGen = System.currentTimeMillis();
		// System.out.println("COllect word: " + (eCollect - sCollect) / (float)
		// 60000);
		// System.out.println("Gen: " + (eGen - eCollect) / (float) 60000);
		// System.out.println("-------------Done!--------------");

		// int train = 800;
		// int test = 1000;
		// long sCollect = System.currentTimeMillis();
		// new CollectWord(this.wordSegment).collectWord(train);
		// long eCollect = System.currentTimeMillis();
		//
		// System.out.println("\n\n\n-------------Starting generate Training
		// File--------------");
		// genTraningData1(train, test);
		//
		// long eGen = System.currentTimeMillis();
		// System.out.println("COllect word: " + (eCollect - sCollect) / (float)
		// 60000);
		// System.out.println("Gen: " + (eGen - eCollect) / (float) 60000);
		// System.out.println("-------------Done!--------------");
	}

	/**
	 * Gen training file va test file theo top (value) comment khong phan biet
	 * nhan. vi du 5000 comment dau tien
	 * 
	 * @param limitTrain
	 * @param limitComment
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void genTraningData(int limitTrain, int limitComment)
			throws SQLException, IOException, InterruptedException {
		ResultSet rs = this.commentDAO.getData("select top " + limitComment
				+ " * from TblCmt where label2 = 1 or label2 = 3 order by id ");
		int dem = 0;
		while (rs.next()) {
			dem++;
			String line = "", line1 = "";
			line += rs.getString(1) + " ";

			line += rs.getString(15) + " ";
			line1 += rs.getString(15) + " ";

			ArrayList<Word> listWord = this.wordSegment.string2Vector(rs.getString(3));

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
					}else{
						line += listWord.get(i).getId() + ":0 ";
						line1 += listWord.get(i).getId() + ":0 ";
					}
				} else {
					System.out
							.println(rs.getString(1) + "khong co tu " + listWord.get(i).getWord());
					Thread.sleep(5000);
				}
			}
			if (line.length() > 7) {
				if (dem <= limitTrain) {
					this.bw.write(line);
					this.bw2.write(line1);
					this.bw.newLine();
					this.bw2.newLine();
				} else {
					this.bw1.write(line);
					this.bw3.write(line1);
					this.bw1.newLine();
					this.bw3.newLine();
				}
			} else {
				System.out.println("LINE " + line + " co length < 7");
				Thread.sleep(5000);
			}
			System.out.println(rs.getString(1) + " done!");
		}
		this.bw.close();
		this.bw1.close();
		this.bw2.close();
		this.bw3.close();
	}

	/**
	 * Gen training data and test file theo top comment cua moi nhan. vi du 1000
	 * comment nhan 1 thi dua vao training 800 va 200 lam test
	 * 
	 * @param train
	 * @param test
	 * @throws SQLException
	 * @throws IOException
	 */
	private void genTraningData1(int train, int test) throws SQLException, IOException {
		ResultSet rs1 = this.commentDAO
				.getData("select top " + test + " * from TblCmt where label = 1 order by id ");
		ResultSet rs2 = this.commentDAO
				.getData("select top " + test + " * from TblCmt where label = 2 order by id ");
		int dem = 0;
		while (rs1.next()) {
			dem++;
			String line = "", line1 = "";
			line += rs1.getString(1) + " ";

			line += rs1.getString(15) + " ";
			line1 += rs1.getString(15) + " ";

			ArrayList<Word> listWord = this.wordSegment.string2Vector(rs1.getString(3));

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
					line += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
					line1 += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
				}
			}
			if (line.length() > 7) {
				if (dem <= train) {
					this.bw.write(line);
					this.bw2.write(line1);
					this.bw.newLine();
					this.bw2.newLine();
				} else {
					this.bw1.write(line);
					this.bw3.write(line1);
					this.bw1.newLine();
					this.bw3.newLine();
				}
			}
			System.out.println(rs1.getString(1) + " done!");
		}

		dem = 0;
		while (rs2.next()) {
			dem++;
			String line = "", line1 = "";
			line += rs2.getString(1) + " ";

			line += rs2.getString(15) + " ";
			line1 += rs2.getString(15) + " ";

			ArrayList<Word> listWord = this.wordSegment.string2Vector(rs2.getString(3));

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
					line += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
					line1 += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
				}
			}
			if (line.length() > 7) {
				if (dem <= train) {
					this.bw.write(line);
					this.bw2.write(line1);
					this.bw.newLine();
					this.bw2.newLine();
				} else {
					this.bw1.write(line);
					this.bw3.write(line1);
					this.bw1.newLine();
					this.bw3.newLine();
				}
			}
			System.out.println(rs2.getString(1) + " done!");
		}

		this.bw.close();
		this.bw1.close();
		this.bw2.close();
		this.bw3.close();
	}

	public void genAllLabel(int train, int limit) throws IOException, SQLException {
		ResultSet rs = this.commentDAO.getData("select top " + limit + " * from TblCmt order by id ");
		int dem = 0;
		while (rs.next()) {
			dem++;
			String line = "", line1 = "";
			line += rs.getString(1) + " ";

			line += rs.getString(15) + " ";
			line1 += rs.getString(15) + " ";

			ArrayList<Word> listWord = this.wordSegment.string2Vector(rs.getString(3));

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
					line += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
					line1 += listWord.get(i).getId() + ":" + listWord.get(i).getTFIDF() + " ";
				}
			}
			if (line.length() > 7) {
				if (dem <= train) {
					this.bw.write(line);
					this.bw2.write(line1);
					this.bw.newLine();
					this.bw2.newLine();
				} else {
					this.bw1.write(line);
					this.bw3.write(line1);
					this.bw1.newLine();
					this.bw3.newLine();
				}
			}
			System.out.println(rs.getString(1) + " done!");
		}
		this.bw.close();
		this.bw1.close();
		this.bw2.close();
		this.bw3.close();
	}
}
