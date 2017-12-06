package ptit.nhunh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.model.Article;

public class ArticleDAO implements SQLDAO {
	private static ArticleDAO instance = new ArticleDAO();
	public static final int UPDATE_NEEDED = 1;
	public static final int UPDATE_TITLES_AND_TAGS = 2;

	private Connection con = null;
	private PreparedStatement ps = null;
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Capstone";
	private String user = "sa";
	private String pass = "123";

	private ArticleDAO() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			this.con = DriverManager.getConnection(this.url, this.user, this.pass);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static ArticleDAO getInstance() {
		return ArticleDAO.instance;
	}

	@Override
	public boolean insert(Object obj) {
		try {
			Article url = (Article) obj;
			this.ps = this.con.prepareStatement("insert into TblArticle values(?,?,?,?,?,?,?,?,?,?,?,?) ");
			this.ps.setNString(1, url.getUrl());
			this.ps.setNString(2, url.getUrl_id());
			this.ps.setNString(3, url.getTitle());
			this.ps.setInt(4, url.getNeeded());
			this.ps.setNString(5, url.getSource());
			this.ps.setInt(6, url.getTotalComment());
			this.ps.setInt(7, url.getTotalParComment());
			this.ps.setNString(8, url.getTag());
			this.ps.setNString(9, url.getCategory());
			this.ps.setDate(10, url.getCreationTime());
			this.ps.setNString(11, url.getContentFilePath());
			this.ps.setNString(12, url.getImageUrl());
			this.ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public ArrayList<Object> getAll() throws SQLException {
		ArrayList<Object> ac = new ArrayList<>();
		this.ps = this.con.prepareStatement("select * from TblArticle order by id");
		ResultSet rs = this.ps.executeQuery();
		while (rs.next()) {
			Article url = new Article(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4), rs.getInt(5),
					rs.getNString(6), rs.getInt(7), rs.getInt(8), rs.getNString(9), rs.getNString(10), rs.getDate(11),
					rs.getNString(12), rs.getNString(13), rs.getInt(14), rs.getInt(15), rs.getInt(16), rs.getInt(17),
					rs.getInt(18));
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
			Article url = new Article(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4), rs.getInt(5),
					rs.getNString(6), rs.getInt(7), rs.getInt(8), rs.getNString(9), rs.getNString(10), rs.getDate(11),
					rs.getNString(12), rs.getNString(13), rs.getInt(14), rs.getInt(15), rs.getInt(16), rs.getInt(17),
					rs.getInt(18));
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
		Article url = (Article) obj;
		try {
			String sql = "";
			switch (field) {
			case UPDATE_TITLES_AND_TAGS:
				sql = "update TblArticle set title = ?, tag = ? where id = ?";
				this.ps = this.con.prepareStatement(sql);
				this.ps.setNString(1, url.getTitle());
				this.ps.setNString(2, url.getTag());
				this.ps.setInt(3, url.getId());
				this.ps.executeUpdate();
				return true;
			case UPDATE_NEEDED:
				sql = "update TblArticle set needed = ? where id = ?";

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
	public Object findByItemId(String id) throws SQLException {
		String sql = "select * from TblArticle where url_id = ?";
		this.ps = this.con.prepareStatement(sql);
		this.ps.setString(1, id);
		ResultSet rs = this.ps.executeQuery();
		rs.next();
		Article url = new Article(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4), rs.getInt(5),
				rs.getNString(6), rs.getInt(7), rs.getInt(8), rs.getNString(9), rs.getNString(10), rs.getDate(11),
				rs.getNString(12), rs.getNString(13), rs.getInt(14), rs.getInt(15), rs.getInt(16), rs.getInt(17),
				rs.getInt(18));
		return url;
	}

	@Override
	public Object findById(String id) throws SQLException {
		String sql = "select * from TblArticle where id = ?";
		this.ps = this.con.prepareStatement(sql);
		this.ps.setString(1, id);
		ResultSet rs = this.ps.executeQuery();
		rs.next();
		Article url = new Article(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4), rs.getInt(5),
				rs.getNString(6), rs.getInt(7), rs.getInt(8), rs.getNString(9), rs.getNString(10), rs.getDate(11),
				rs.getNString(12), rs.getNString(13), rs.getInt(14), rs.getInt(15), rs.getInt(16), rs.getInt(17),
				rs.getInt(18));
		return url;
	}

	@Override
	public boolean update(Object obj) throws SQLException {
		Article url = (Article) obj;
		String sql = "update TblArticle set url = ?, url_id = ?, title = ?, needed = ?, source = ?, totalCmt = ?, totalParCmt = ?, tag = ?, category = ?, creationTime = ?, contentFilePath = ?, imageUrl = ?, dongy = ?, khongdongy = ?, gopy = ?, ykienkhac = ? where id = ?";
		this.ps = this.con.prepareStatement(sql);
		this.ps.setNString(1, url.getUrl());
		this.ps.setNString(2, url.getUrl_id());
		this.ps.setNString(3, url.getTitle());
		this.ps.setInt(4, url.getNeeded());
		this.ps.setNString(5, url.getSource());
		this.ps.setInt(6, url.getTotalComment());
		this.ps.setInt(7, url.getTotalParComment());
		this.ps.setNString(8, url.getTag());
		this.ps.setNString(9, url.getCategory());
		this.ps.setDate(10, url.getCreationTime());
		this.ps.setNString(11, url.getContentFilePath());
		this.ps.setNString(12, url.getImageUrl());
		this.ps.setInt(13, url.getDongy());
		this.ps.setInt(14, url.getKhongdongy());
		this.ps.setInt(15, url.getGopy());
		this.ps.setInt(16, url.getYkienkhac());
		this.ps.setInt(17, url.getId());
		this.ps.executeUpdate();
		return true;
	}
}
