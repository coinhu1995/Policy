package test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import ptit.nhunh.context.Context;
import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;

public class test2 {
	public static void main(String[] args) throws IOException, SQLException {
		SQLDAO cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		SQLDAO cmtTestDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
		Scanner scan1 = null, scan2 = null;
		if (Context.TYPEOFCOPYDATA2DATABASE == 1) {
			scan1 = new Scanner(new File("src\\main\\resources\\data\\100\\1_150.txt"));
			scan2 = new Scanner(new File("src\\main\\resources\\data\\100\\2_150.txt"));
		} else if (Context.TYPEOFCOPYDATA2DATABASE == 2) {
			scan1 = new Scanner(new File("src\\main\\resources\\data\\100\\1_100.txt"));
			scan2 = new Scanner(new File("src\\main\\resources\\data\\100\\2_100.txt"));
		}
		cmtTestDao
				.update("delete from TblCommentTest DBCC CHECKIDENT ('TblCommentTest', RESEED, 0)");
		ArrayList<Comment> label1 = new ArrayList<>();
		ArrayList<Comment> label2 = new ArrayList<>();
		while (scan1.hasNextLine()) {
			String line = scan1.nextLine();
			Comment c = (Comment) cmtDao.findById(line.split(" ")[1].trim());
			c.setLabel(1);
			label1.add(c);

			String line2 = scan2.nextLine();
			Comment c2 = (Comment) cmtDao.findById(line2.split(" ")[1].trim());
			c2.setLabel(2);
			label2.add(c2);
		}

		for (int i = 0; i < Context.TRAINSIZE / 2; i++) {
			cmtTestDao.insert(label1.get(i));
		}
		for (int i = 0; i < Context.TRAINSIZE / 2; i++) {
			cmtTestDao.insert(label2.get(i));
		}
		for (int i = Context.TRAINSIZE / 2; i < (Context.TESTSIZE + Context.TRAINSIZE) / 2; i++) {
			cmtTestDao.insert(label1.get(i));
		}
		for (int i = Context.TRAINSIZE / 2; i < (Context.TESTSIZE + Context.TRAINSIZE) / 2; i++) {
			cmtTestDao.insert(label2.get(i));
		}
		scan1.close();
		scan2.close();
		System.out.println("done");
	}
}
