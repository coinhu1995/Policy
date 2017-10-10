package ptit.nhunh.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;

/**
 * Đọc từ file tự gán nhãn và update vào database
 * 
 * @author coinh
 *
 */
public class ReadResult {
	private SQLDAO articleDao = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);

	public static void main(String[] args) throws IOException {
		new ReadResult().execute();
	}

	public void execute() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("result.txt")));

		String line = "";
		while ((line = br.readLine()) != null) {
			String id = line.substring(0, 6).trim();
			String label = line.substring(6).trim();

			System.out.println(this.articleDao.update("update TblComment set label = " + label + "where id = " + id));
		}
	}
}
