package ptit.nhunh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ptit.nhunh.model.Url;

public abstract class DBDAO {
	public Connection con = null;
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=";
	private String user = "sa";
	private String pass = "123";

	public DBDAO(String dbName) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			this.url += dbName;
			this.con = DriverManager.getConnection(this.url, this.user, this.pass);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	public ResultSet getData(String sql) throws SQLException {
		Statement st = this.con.createStatement();
		return st.executeQuery(sql);
	}

	public void updateData(String sql) throws SQLException {
		Statement st = this.con.createStatement();
		st.executeUpdate(sql);
	}

	public void close() {
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
