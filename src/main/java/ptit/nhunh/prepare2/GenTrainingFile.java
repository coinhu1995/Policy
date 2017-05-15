package ptit.nhunh.prepare2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.model.Word;
import ptit.nhunh.utils.Utils;

public class GenTrainingFile {
	private CommentDAO commentDAO;

	public GenTrainingFile() {
		this.commentDAO = new CommentDAO("Capstone");
	}
	
	public void process(int train, int test) throws SQLException, IOException, InterruptedException {
		genDataFile(train, test);
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
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\2label\\train"))));
		BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\2label\\test"))));
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\2label\\input.train"))));
		BufferedWriter bw3 = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src\\main\\resource\\data\\2label\\input.test"))));

		ResultSet rs = this.commentDAO
				.getData("select top " + test + " * from TblComment order by id ");
		int dem = 0;
		int percent = 0;
		while (rs.next()) {
			dem++;
			String line = "", line1 = "";
			line += rs.getString(1) + " ";

			// for label
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

			int pc = (int) (dem / (float) test * 100);
			if (pc > percent) {
				percent = pc;
				System.out.println("\t\t" + percent + "%");
			}
		}
		bw.close();
		bw1.close();
		bw2.close();
		bw3.close();
	}
}
