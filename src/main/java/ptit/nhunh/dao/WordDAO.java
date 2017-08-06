package ptit.nhunh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.model.Word;

public class WordDAO implements SQLDAO {
	private static WordDAO instance = new WordDAO();

	private Connection con = null;
	private PreparedStatement ps = null;
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Capstone";
	private String user = "sa";
	private String pass = "123";

	private WordDAO() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			this.con = DriverManager.getConnection(this.url, this.user, this.pass);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static WordDAO getInstance() {
		return WordDAO.instance;
	}

	@Override
	public boolean insert(Object obj) {
		Word word = (Word) obj;
		String sql = "insert into TblWord values(?,?,?,?,?) ";
		try {
			this.ps = this.con.prepareStatement(sql);
			this.ps.setNString(1, word.getWord());
			this.ps.setInt(2, word.getDF());
			this.ps.setInt(4, word.getIsStop());
			this.ps.setInt(5, word.getCmt_id());
			this.ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ArrayList<Object> getAll() throws SQLException {
		ArrayList<Object> ac = new ArrayList<>();
		this.ps = this.con.prepareStatement("select * from TblWord order by id");
		ResultSet rs = this.ps.executeQuery();
		while (rs.next()) {
//			Word word = new Word(rs.getInt(1), rs.getNString(2), 0, 0, rs.getInt(3), rs.getInt(4),
//					rs.getFloat(5), 0, rs.getInt(6));
//			ac.add(word);
		}
		return ac;
	}

	@Override
	public ArrayList<Object> getData(String sql) throws SQLException {
		ArrayList<Object> ac = new ArrayList<>();
		this.ps = this.con.prepareStatement(sql);
		ResultSet rs = this.ps.executeQuery();
		while (rs.next()) {
//			Word word = new Word(rs.getInt(1), rs.getNString(2), 0, 0, rs.getInt(3), rs.getInt(4),
//					rs.getFloat(5), 0, rs.getInt(6));
//			ac.add(word);
		}
		return ac;
	}

	/**
	 * Update field isStop to Database
	 */
	@Override
	public boolean update(Object obj, int field) {
		Word word = (Word) obj;
		try {
			String sql = "";
			sql = "update TblWord set isStop = ? where id = ?";
			this.ps = this.con.prepareStatement(sql);
			this.ps.setInt(1, word.getIsStop());
			this.ps.setInt(2, word.getId());
			this.ps.executeUpdate();
			return true;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findById(String id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
