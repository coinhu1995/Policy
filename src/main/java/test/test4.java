package test;

import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;

public class test4 {
	public static void main(String[] args) throws SQLException{
		SQLDAO cmtTestDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
		ArrayList<Object> listcmt = cmtTestDao.getAll();
		for(Object obj : listcmt){
			Comment c = (Comment) obj;
			System.out.println(c.getCmt_segment());
		}
	}
}
