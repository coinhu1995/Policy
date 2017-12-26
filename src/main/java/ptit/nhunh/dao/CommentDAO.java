package ptit.nhunh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.model.Comment;

public class CommentDAO implements SQLDAO {
	private static CommentDAO instance = new CommentDAO();
	public static final int UPDATE_COMMENT_SEGMENT = 1;
	public static final int UPDATE_COMMENT_LABEL = 2;
	public static final int UPDATE_COMMENT_LABEL_2 = 3;

	private Connection con = null;
	private PreparedStatement ps = null;
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Capstone";
	private String user = "sa";
	private String pass = "123";

	private CommentDAO() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			this.con = DriverManager.getConnection(this.url, this.user, this.pass);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static CommentDAO getInstance() {
		return CommentDAO.instance;
	}

	@Override
	public boolean insert(Object obj) throws SQLException {
		Comment c = (Comment) obj;
		String sql = "insert into TblComment values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		this.ps = this.con.prepareStatement(sql);
		this.ps.setNString(1, c.getCmt_id());
		this.ps.setNString(2, c.getContent());
		this.ps.setNString(3, c.getCmt_segment());
		this.ps.setNString(4, c.getTime());
		this.ps.setNString(5, c.getPage_id());
		this.ps.setNString(6, c.getAvatar_original());
		this.ps.setNString(7, c.getLike_ismember());
		this.ps.setNString(8, c.getUser_id());
		this.ps.setNString(9, c.getCreate_time());
		this.ps.setNString(10, c.getAvatar());
		this.ps.setInt(11, c.getType());
		this.ps.setInt(12, c.getUserlike());
		this.ps.setNString(13, c.getFullname());
		this.ps.setNString(14, c.getParent_id());
		this.ps.setInt(15, c.getLabel());
		this.ps.setInt(16, c.getLabel2());
		this.ps.setInt(17, c.getArticleid());
		this.ps.executeUpdate();
		return true;
	}

	@Override
	public ArrayList<Object> getAll() throws SQLException {
		ArrayList<Object> ac = new ArrayList<>();
		this.ps = this.con.prepareStatement("select * from TblComment order by id");
		ResultSet rs = this.ps.executeQuery();
		while (rs.next()) {
			Comment c = new Comment(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4),
					rs.getNString(5), rs.getNString(6), rs.getNString(7), rs.getNString(8), rs.getNString(9),
					rs.getNString(10), rs.getNString(11), rs.getInt(12), rs.getInt(13), rs.getNString(14),
					rs.getNString(15), rs.getInt(16), rs.getInt(17), rs.getInt(18));
			ac.add(c);
		}
		return ac;
	}

	@Override
	public ArrayList<Object> getData(String sql) throws SQLException {
		ArrayList<Object> ac = new ArrayList<>();
		this.ps = this.con.prepareStatement(sql);
		ResultSet rs = this.ps.executeQuery();
		while (rs.next()) {
			Comment c = new Comment(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4),
					rs.getNString(5), rs.getNString(6), rs.getNString(7), rs.getNString(8), rs.getNString(9),
					rs.getNString(10), rs.getNString(11), rs.getInt(12), rs.getInt(13), rs.getNString(14),
					rs.getNString(15), rs.getInt(16), rs.getInt(17), rs.getInt(18));
			ac.add(c);
		}
		return ac;
	}

	/**
	 * field: <br>
	 * <strong>UPDATE_COMMENT_SEGMENT<strong> for update field cmt_segment
	 * <strong>UPDATE_COMMENT_LABEL<strong> for update field label
	 * <strong>UPDATE_COMMENT_LABEL_2<strong> for update field label2
	 */
	@Override
	public boolean update(Object obj, int field) {
		Comment c = (Comment) obj;
		try {
			String sql = "";
			switch (field) {
			case UPDATE_COMMENT_SEGMENT:
				sql = "update TblComment set cmt_segment = ? where id = ?";
				this.ps = this.con.prepareStatement(sql);
				this.ps.setNString(1, c.getCmt_segment());
				this.ps.setInt(2, c.getId());
				this.ps.executeUpdate();
				break;
			case UPDATE_COMMENT_LABEL:
				sql = "update TblComment set label = ? where id = ?";
				this.ps = this.con.prepareStatement(sql);
				this.ps.setInt(1, c.getLabel());
				this.ps.setInt(2, c.getId());
				this.ps.executeUpdate();
				break;
			case UPDATE_COMMENT_LABEL_2:
				sql = "update TblComment set label2 = ? where id = ?";
				this.ps = this.con.prepareStatement(sql);
				this.ps.setInt(1, c.getLabel2());
				this.ps.setInt(2, c.getId());
				this.ps.executeUpdate();
				break;
			}
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean update(String sql) {
		try {
			this.ps = this.con.prepareStatement(sql);
			this.ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Object findByItemId(String id) throws SQLException {
		this.ps = this.con.prepareStatement("select * from TblComment where cmt_id = ?");
		this.ps.setString(1, id);
		ResultSet rs = this.ps.executeQuery();
		rs.next();
		Comment c = new Comment(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4), rs.getNString(5),
				rs.getNString(6), rs.getNString(7), rs.getNString(8), rs.getNString(9), rs.getNString(10),
				rs.getNString(11), rs.getInt(12), rs.getInt(13), rs.getNString(14), rs.getNString(15), rs.getInt(16),
				rs.getInt(17), rs.getInt(18));
		return c;
	}

	@Override
	public Object findById(String id) throws SQLException {
		this.ps = this.con.prepareStatement("select * from TblComment where id = ?");
		this.ps.setString(1, id);
		ResultSet rs = this.ps.executeQuery();
		rs.next();
		Comment c = new Comment(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4), rs.getNString(5),
				rs.getNString(6), rs.getNString(7), rs.getNString(8), rs.getNString(9), rs.getNString(10),
				rs.getNString(11), rs.getInt(12), rs.getInt(13), rs.getNString(14), rs.getNString(15), rs.getInt(16),
				rs.getInt(17), rs.getInt(18));
		return c;
	}

	@Override
	public boolean update(Object obj) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}
