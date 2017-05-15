package ptit.nhunh.tool;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.DBDAO;

public class CountLabelInDB {
	public static void main(String[] args) throws FileNotFoundException, SQLException {
		CommentDAO commentDAO = new CommentDAO("Capstone");
		int limit = 700;
		ResultSet rs = commentDAO.getData("select top 909 * from TblComment order by id");
		int dem = 0;
		int trainCount_1 = 0, trainCount_2 = 0, trainCount_3 = 0, trainCount_4 = 0,
				trainCount_5 = 0;
		int testCount_1 = 0, testCount_2 = 0, testCount_3 = 0, testCount_4 = 0, testCount_5 = 0;

		while (rs.next()) {
			dem++;
			String s = rs.getString(16);

			if (s.charAt(0) == '1') {
				if (dem <= limit) {
					trainCount_1++;
				} else {
					testCount_1++;
				}
			}
			if (s.charAt(0) == '2') {
				if (dem <= limit) {
					trainCount_2++;
				} else {
					testCount_2++;
				}
			}
			if (s.charAt(0) == '3') {
				if (dem <= limit) {
					trainCount_3++;
				} else {
					testCount_3++;
				}
			}
			if (s.charAt(0) == '4') {
				if (dem <= limit) {
					trainCount_4++;
				} else {
					testCount_4++;
				}
			}
			if (s.charAt(0) == '5') {
				if (dem <= limit) {
					trainCount_5++;
				} else {
					testCount_5++;
				}
			}
		}
		System.out.println("-------Train-------");
		System.out.println("Label 1: " + trainCount_1);
		System.out.println("Label 2: " + trainCount_2);
		System.out.println("Label 3: " + trainCount_3);
		System.out.println("Label 4: " + trainCount_4);
		System.out.println("Label 5: " + trainCount_5);
		
		System.out.println("-------Test-------");
		System.out.println("Label 1: " + testCount_1);
		System.out.println("Label 2: " + testCount_2);
		System.out.println("Label 3: " + testCount_3);
		System.out.println("Label 4: " + testCount_4);
		System.out.println("Label 5: " + testCount_5);
		System.out.println("\nTong: "+dem);
	}
}
