package ptit.nhunh.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.model.Url;

public class UrlDAO extends DBDAO {
	public UrlDAO(String dbName) {
		super(dbName);
	}
	
	public static void main(String[] args) {
		Url url  = new Url();
		new UrlDAO("Capstone").insertUrl(url);;
	}
	
	public void insertUrl(Url u){
		try {
			this.updateData("insert into TblUrl values(N'" + u.getUrl() + "','" + u.getUrl_id() + "',N'"
					+ u.getTittle() + "','" + u.getNeeded() + "',N'" + u.getSource() + "',"
					+ u.getTotalComment() + " ," + u.getTotalParComment() + " ,N'" + u.getTag()
					+ "')  ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Url> getAllUrl() throws SQLException {
		ArrayList<Url> listURL = new ArrayList<>();
		ResultSet rs = this.getData(
				"select * from TblUrl order by id");
		while (rs.next()) {
			Url url = new Url(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getString(9));
			listURL.add(url);
		}
		return listURL;
	}

	public ArrayList<Url> getAllNeededUrl() throws SQLException {
		ArrayList<Url> listURL = new ArrayList<>();
		ResultSet rs = this.getData(
				"select distinct * from TblUrl where url_id not in (select distinct article_id from TblCmt) and needed = 1 and totalParCmt > 0");
		while (rs.next()) {
			Url url = new Url(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getString(9));
			listURL.add(url);
		}
		return listURL;
	}
	
	public ArrayList<Url> getThanhNienUrl() throws SQLException {
		ArrayList<Url> listURL = new ArrayList<>();
		ResultSet rs = this.getData(
				"select * from TblUrl where source = 'thanhnien' order by id");
		while (rs.next()) {
			Url url = new Url(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getString(9));
			listURL.add(url);
		}
		return listURL;
	}
}
