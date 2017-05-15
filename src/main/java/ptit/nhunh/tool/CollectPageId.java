package ptit.nhunh.tool;

import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.UrlDAO;

public class CollectPageId {
	public static void main(String[] args) throws SQLException {
		UrlDAO dbc = new UrlDAO("Capstone");
		ResultSet rs = dbc.getData("select * from TblUrl order by id");
		while(rs.next()){
			String pageId = getPageId(rs.getString(2));
			dbc.updateData("update TblUrl set url_id = '"+pageId+"' where id = "+rs.getInt(1));
		}
	}
	private static String getPageId(String url) {
		return url.substring(url.lastIndexOf("-") + 1, url.lastIndexOf("-") + 8);
	}
}
