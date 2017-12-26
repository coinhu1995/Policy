package ptit.nhunh.standard;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;

public class ReadData {
	private SQLDAO cmtDao;
	private SQLDAO cmtTestDao;
	private int train = 100;

	public static void main(String[] args) throws FileNotFoundException, SQLException {
		new ReadData().process();
	}

	public ReadData() {
		this.cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.cmtTestDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
	}

	private void process() throws FileNotFoundException, SQLException {
		this.cmtTestDao
				.update("delete from TblCommentTest DBCC CHECKIDENT ('TblCommentTest', RESEED, 0)");

		Scanner scan1 = new Scanner(new File("src\\main\\resources\\data\\100\\1_150.txt"));
		Scanner scan2 = new Scanner(new File("src\\main\\resources\\data\\100\\2_150.txt"));
		
//		Scanner scan1 = new Scanner(new File("src\\main\\resources\\data\\100\\1_100.txt"));
//		Scanner scan2 = new Scanner(new File("src\\main\\resources\\data\\100\\2_100.txt"));
		
		ArrayList<Comment> listCmt1 = new ArrayList<>();
		ArrayList<Comment> listCmt2 = new ArrayList<>();
		while (scan1.hasNext()) {
			String line = scan1.nextLine();
			line = line.substring(0, 13);
			Comment c = (Comment) this.cmtDao.findById(line.split(" ")[1].trim());
			c.setLabel(1);
			listCmt1.add(c);
		}
		while (scan2.hasNext()) {
			String line = scan2.nextLine();
			line = line.substring(0, 13);
			Comment c = (Comment) this.cmtDao.findById(line.split(" ")[1].trim());
			c.setLabel(2);
			listCmt2.add(c);
		}
		for (int i = 0; i < this.train; i++) {
			this.cmtTestDao.insert(listCmt1.get(i));
		}
		for (int i = 0; i < this.train; i++) {
			this.cmtTestDao.insert(listCmt2.get(i));
		}
		for (int i = this.train; i < listCmt1.size(); i++) {
			this.cmtTestDao.insert(listCmt1.get(i));
		}
		for (int i = this.train; i < listCmt2.size(); i++) {
			this.cmtTestDao.insert(listCmt2.get(i));
		}
		scan1.close();
		scan2.close();
		System.out.println("Done");
	}
}
