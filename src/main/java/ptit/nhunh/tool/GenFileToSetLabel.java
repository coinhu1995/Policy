package ptit.nhunh.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.DBDAO;

public class GenFileToSetLabel {
	public static void main(String[] args) throws SQLException, IOException {
		CommentDAO commentDAO = new CommentDAO("Capstone");
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("comment.txt"))));
		ResultSet rs = commentDAO.getData(
				"select id, comment_id, label, cmt_content, article_id from TblCmt order by id");

		while (rs.next()) {
			bw.write(String.format("%-6s", rs.getString(1)));
			bw.write(String.format("%-10s", rs.getString(2)));
			bw.write(String.format("%-3s", rs.getString(3)));
			bw.write(rs.getString(4));
			bw.newLine();
			ResultSet rs2 = commentDAO
					.getData("select * from TblUrl where url_id = '" + rs.getString(5) + "'  ");
			rs2.next();
			bw.write(rs2.getString(2));
			bw.newLine();
		}

		bw.close();
	}
}
