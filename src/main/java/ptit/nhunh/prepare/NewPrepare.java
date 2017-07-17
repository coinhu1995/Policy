package ptit.nhunh.prepare;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;

public class NewPrepare {
	public static void main(String[] args) throws SQLException, IOException {
		new NewPrepare().gen();
	}

	public void gen() throws SQLException, IOException {
		Scanner scan = new Scanner(System.in);
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("200.txt", true)));
		SQLDAO dao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		ArrayList<Object> listCmt = dao.getAll();
		int dem_1 = 0, dem_2 = 0, dem_3 = 0;
		int a = 96;
		for (int i = a; i < listCmt.size(); i++) {
			Comment c = (Comment) listCmt.get(i);
			if (c.getLabel() == 1) {
				System.out.println(c.getContent());
				if (scan.nextLine().trim().equals("1") && dem_1 < 201) {
					bw.write(String.format("1 " + "%-12s", c.getCmt_id()) + c.getCmt_segment());
					bw.newLine();
					bw.flush();
					dem_1++;
					System.out.println(dem_1);
				}
				if (dem_1 > 200) {
					break;
				}
			}
			System.out.println("i = " + i);
		}
		for (int i = 0; i < listCmt.size(); i++) {
			Comment c = (Comment) listCmt.get(i);
			if (c.getLabel() == 2) {
				System.out.println(c.getContent());
				if (scan.nextLine().trim().equals("2") && dem_2 < 201) {
					bw.write(String.format("2 " + "%-12s", c.getCmt_id()) + c.getCmt_segment());
					bw.newLine();
					bw.flush();
					dem_2++;
					System.out.println(dem_2);
				}
				if (dem_2 > 200) {
					break;
				}
			}
			System.out.println("i = " + i);
		}
		for (int i = 0; i < listCmt.size(); i++) {
			Comment c = (Comment) listCmt.get(i);
			if (c.getLabel() == 3) {
				System.out.println(c.getContent());
				if (scan.nextLine().trim().equals("3") && dem_3 < 201) {
					bw.write(String.format("3 " + "%-12s", c.getCmt_id()) + c.getCmt_segment());
					bw.newLine();
					bw.flush();
					dem_3++;
					System.out.println(dem_3);
				}
				if (dem_3 > 200) {
					break;
				}
			}
			System.out.println("i = " + i);
		}

		scan.close();
		bw.close();
	}
}
