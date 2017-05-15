package ptit.nhunh.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.model.Comment;

public class CommentDAO extends DBDAO {

	public CommentDAO(String dbName) {
		super(dbName);
	}

	public static void main(String[] args) throws SQLException {
		Comment c = new Comment();
		new CommentDAO("Capstone").insertCmt(c);
	}

	public void insertCmt(Comment c) throws SQLException {
		this.updateData("insert into TblComment values('" + c.getCmt_id() + "',N'" + c.getContent()
				+ "',N'" + c.getCmt_segment() + "',N'" + c.getTime() + "','" + c.getPage_id()
				+ "','" + c.getAvatar_original() + "','" + c.getLike_ismember() + "','"
				+ c.getUser_id() + "',N'" + c.getCreate_time() + "','" + c.getAvatar() + "',"
				+ c.getType() + "," + c.getUserlike() + ",N'" + c.getFullname() + "','"
				+ c.getParent_id() + "'," + c.getLabel() + "," + c.getLabel2() + " ) ");
	}

	public ArrayList<Comment> getAllComment() throws SQLException {
		ArrayList<Comment> ac = new ArrayList<>();
		ResultSet rs = this.getData("select * from TblComment order by id");
		while (rs.next()) {
			Comment c = new Comment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
					rs.getString(9), rs.getString(10), rs.getString(11), rs.getInt(12),
					rs.getInt(13), rs.getString(14), rs.getString(15), rs.getInt(16),
					rs.getInt(17));
			ac.add(c);
		}
		return ac;
	}
}
