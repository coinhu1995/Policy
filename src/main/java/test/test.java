package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class test {

	private Connection con = null;
	private PreparedStatement ps = null;
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Capstone";
	private String user = "sa";
	private String pass = "123";

	public test() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			this.con = DriverManager.getConnection(this.url, this.user, this.pass);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void delete() throws SQLException {
		this.ps = this.con.prepareStatement("update Tblabx set label = 1 where label = 100");
		System.out.println(this.ps.executeUpdate());
	}

	public static void main(String[] args) throws IOException, SQLException {
		test t = new test();
		t.delete();
	}
}
