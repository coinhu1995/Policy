package ptit.nhunh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.model.Url;

public class UrlDAO implements SQLDAO {
	private static UrlDAO instance = new UrlDAO();
	public static final int UPDATE_NEEDED = 1;
	public static final int UPDATE_TITLES_AND_TAGS = 2;

	private Connection con = null;
	private PreparedStatement ps = null;
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Capstone";
	private String user = "sa";
	private String pass = "123";

	private UrlDAO() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			this.con = DriverManager.getConnection(this.url, this.user, this.pass);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static UrlDAO getInstance() {
		return UrlDAO.instance;
	}

	@Override
	public boolean insert(Object obj) {
		try {
			Url url = (Url) obj;
			this.ps = this.con.prepareStatement("insert into TblUrl values(?,?,?,?,?,?,?,?) ");
			this.ps.setNString(1, url.getUrl());
			this.ps.setNString(2, url.getUrl_id());
			this.ps.setNString(3, url.getTitles());
			this.ps.setInt(4, url.getNeeded());
			this.ps.setNString(5, url.getSource());
			this.ps.setInt(6, url.getTotalComment());
			this.ps.setInt(7, url.getTotalParComment());
			this.ps.setNString(8, url.getTag());
			this.ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public ArrayList<Object> getAll() throws SQLException {
		ArrayList<Object> ac = new ArrayList<>();
		this.ps = this.con.prepareStatement("select * from TblUrl order by id");
		ResultSet rs = this.ps.executeQuery();
		while (rs.next()) {
			Url url = new Url(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4),
					rs.getInt(5), rs.getNString(6), rs.getInt(7), rs.getInt(8), rs.getNString(9));
			ac.add(url);
		}
		return ac;
	}

	@Override
	public ArrayList<Object> getData(String sql) throws SQLException {
		ArrayList<Object> ac = new ArrayList<>();
		this.ps = this.con.prepareStatement(sql);
		ResultSet rs = this.ps.executeQuery();
		while (rs.next()) {
			Url url = new Url(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4),
					rs.getInt(5), rs.getNString(6), rs.getInt(7), rs.getInt(8), rs.getNString(9));
			ac.add(url);
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
		Url url = (Url) obj;
		try {
			String sql = "";
			switch (field) {
			case UPDATE_TITLES_AND_TAGS:
				sql = "update TblUrl set titles = ? and tag = ? where id = ?";
				this.ps = this.con.prepareStatement(sql);
				this.ps.setNString(1, url.getTitles());
				this.ps.setNString(2, url.getTag());
				this.ps.setInt(3, url.getId());
				this.ps.executeUpdate();
				return true;
			case UPDATE_NEEDED:
				sql = "update TblUrl set needed = ? where id = ?";

				this.ps = this.con.prepareStatement(sql);
				this.ps.setInt(1, url.getNeeded());
				this.ps.setInt(2, url.getId());
				this.ps.executeUpdate();
				return true;
			default:
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean update(String sql) {
		try {
			this.ps = this.con.prepareStatement(sql);
			this.ps.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	@Override
	public Object find(String id) throws SQLException {
		String sql = "select * from TblUrl where url_id = ?";
		this.ps = this.con.prepareStatement(sql);
		this.ps.setString(1, id);
		ResultSet rs = this.ps.executeQuery();
		rs.next();
		Url url = new Url(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4),
				rs.getInt(5), rs.getNString(6), rs.getInt(7), rs.getInt(8), rs.getNString(9));
		return url;
	}
}
