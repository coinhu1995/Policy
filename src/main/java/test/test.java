package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;

public class test {
	public static void main(String[] args) throws IOException, SQLException {
		Scanner scan = new Scanner(new File("result.txt"));
		Map result = new HashMap<>();
		int dem = 1;
		while (scan.hasNext()) {
			String[] row = scan.nextLine().split("\t");
			int dem1 = 0, dem2 = 0, dem3 = 0;
			for (int i = 1; i < row.length; i++) {
				if (row[i].equals("1")) {
					dem1++;
				} else if (row[i].equals("2")) {
					dem2++;
				} else if (row[i].equals("3")) {
					dem3++;
				}
			}
			int max = Math.max(Math.max(dem1, dem2), dem3);
			if (dem1 == max) {
				result.put(dem, 1);
			} else if (dem2 == max) {
				result.put(dem, 2);
			} else {
				result.put(dem, 3);
			}
			dem++;
		}
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("ketqua.txt")));
		SQLDAO cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		result.forEach((k, v) -> {
			// cmtDao.update("update TblComment set label = " + v + " where id =
			// " + k);
			try {
				bw.write(k + "\t" + v);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		bw.close();
	}
}
