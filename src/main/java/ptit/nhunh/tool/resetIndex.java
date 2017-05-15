package ptit.nhunh.tool;

import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.dao.WordDAO;

public class resetIndex {
	private UrlDAO urlDAO;
	private CommentDAO commentDAO;
	private WordDAO wordDAO;

	public resetIndex() {
		this.urlDAO = new UrlDAO("Capstone");
		this.commentDAO = new CommentDAO("Capstone");
		this.wordDAO = new WordDAO("Capstone");
	}

	public static void main(String[] args) throws SQLException {
		new resetIndex().resetIndexTblComment();
	}

	private void resetIndexTblComment() throws SQLException {
		ResultSet rs = this.commentDAO.getData("select * from TblComment order by id");

		this.commentDAO.updateData("delete from TblComment");
		this.commentDAO.updateData("DBCC CHECKIDENT ('TblComment', RESEED, 0)");

		while (rs.next()) {
			String sql = "insert into TblComment values(N'" + rs.getString(2) + "', N'"
					+ rs.getString(3) + "', N'" + rs.getString(4) + "', N'" + rs.getString(5) + "N','"
					+ rs.getString(6) + "',N'" + rs.getString(7) + "', N'" + rs.getString(8) + "N', '"
					+ rs.getString(9) + "', N'" + rs.getString(10) + "', N'" + rs.getString(11)
					+ "', N'" + rs.getString(12) + "', N'" + rs.getString(13) + "', N'"
					+ rs.getString(14) + "', N'" + rs.getString(15) + "', N'" + rs.getString(16)
					+ "', N'" + rs.getString(17) + "')";
			this.commentDAO.updateData(sql);
		}
	}

	private void resetIndexTblUrl() throws SQLException {
		ResultSet rs = this.urlDAO.getData("select * from TblUrl order by id");

		this.urlDAO.updateData("delete from TblUrl");
		this.urlDAO.updateData("DBCC CHECKIDENT ('TblUrl', RESEED, 0)");

		while (rs.next()) {
			this.urlDAO.updateData("insert into TblUrl values(N'" + rs.getString(2) + "','"
					+ rs.getString(3) + "',N'" + rs.getString(4) + "'," + rs.getInt(5) + ",'"
					+ rs.getString(6) + "','" + rs.getInt(7) + "'," + rs.getInt(8) + ",'"
					+ rs.getString(9) + "') ");
		}
	}
}
