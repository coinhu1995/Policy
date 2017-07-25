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
		int dem_1 = 184, dem_2 = 144, dem_3 = 0;
		int a1 = 4442;
		int a2 = 5522;
		int a3 = 0;
		// for (int i = a1; i < listCmt.size(); i++) {
		// Comment c = (Comment) listCmt.get(i);
		// if (c.getLabel() == 1) {
		// System.out.println(c.getId() + " " + c.getContent());
		// if (scan.nextLine().trim().equals("1") && dem_1 < 201) {
		// bw.write(String.format("1 " + "%-12s", c.getId()) +
		// c.getCmt_segment());
		// bw.newLine();
		// bw.flush();
		// dem_1++;
		// System.out.println("dem: " + dem_1);
		// }
		// if (dem_1 > 191) {
		// break;
		// }
		// scan.reset();
		// }
		// }
		for (int i = a2; i < listCmt.size(); i++) {
			Comment c = (Comment) listCmt.get(i);
			if (c.getLabel() == 2 || c.getLabel() == 0) {
				if (c.getContent().length() > 100) {
					System.out.println(c.getId() + " " + c.getContent().substring(0, 100) + "\n"
							+ c.getContent().substring(100));
				} else {
					System.out.println(c.getId() + "  " + c.getContent());
				}
				if (scan.nextLine().trim().equals("2") && dem_2 < 201) {
					bw.write(String.format("2 " + "%-12s", c.getId()) + c.getCmt_segment());
					bw.newLine();
					bw.flush();
					dem_2++;
					System.out.println("dem: " + dem_2);
				}
				if (dem_2 > 200) {
					break;
				}
			}
		}
		for (int i = a3; i < listCmt.size(); i++) {
			Comment c = (Comment) listCmt.get(i);
			if (c.getLabel() == 3) {
				System.out.println(c.getId() + " " + c.getContent());
				if (scan.nextLine().trim().equals("3") && dem_3 < 201) {
					bw.write(String.format("3 " + "%-12s", c.getId()) + c.getCmt_segment());
					bw.newLine();
					bw.flush();
					dem_3++;
					System.out.println("dem: " + dem_3);
				}
				if (dem_3 > 200) {
					break;
				}
			}
		}

		scan.close();
		bw.close();
	}
}
