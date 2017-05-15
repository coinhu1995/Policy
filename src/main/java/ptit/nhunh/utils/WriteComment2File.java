package ptit.nhunh.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.DBDAO;

public class WriteComment2File {
	public static void main(String[] args) throws SQLException, IOException {
		new WriteComment2File().writeFile("comment");
	}

	public void writeFile(String fileName) throws SQLException, IOException {
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName)));
		CommentDAO dbc = new CommentDAO("Capstone");
		ResultSet rs = dbc.getData("select * from TblCmt order by id");
		while(rs.next()){
			bw.write(rs.getString(2)+ " " + rs.getString(3));
			bw.newLine();
		}
		bw.close();
	}
}
