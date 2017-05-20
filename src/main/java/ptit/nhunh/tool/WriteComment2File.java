package ptit.nhunh.tool;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;

public class WriteComment2File {
	public static void main(String[] args) throws SQLException, IOException {
		new WriteComment2File().writeFile("comment");
	}

	public void writeFile(String fileName) throws SQLException, IOException {
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName)));
		SQLDAO cmtDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		ArrayList<Object> listCmt = cmtDAO.getAll();
		for(Object o: listCmt){
			Comment c = (Comment) o;
			bw.write(c.getCmt_id() + " " +c.getContent());
			bw.newLine();
		}
		bw.close();
	}
}
