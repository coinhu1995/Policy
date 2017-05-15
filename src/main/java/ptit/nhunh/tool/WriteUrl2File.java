package ptit.nhunh.tool;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.UrlDAO;

public class WriteUrl2File {
	public static void main(String[] aegs) throws SQLException, IOException {
//		System.out.println("processing...");
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("url.txt"))));
//		DBConnect dbc = new DBConnect();
//		ResultSet rs = dbc.getData("select * from TblUrl");
//		dbc.updateData("delete from TblUrl DBCC CHECKIDENT ('TblUrl', RESEED, 0)");
//		while (rs.next()) {
//			String url = rs.getString(2);
//			if (url.indexOf("tag") < 0 && url.indexOf("video") < 0 && url.indexOf("topic") < 0
//					&& url.indexOf("/doi-bong/") < 0 && url.indexOf("vnexpress") >=0 && url.indexOf("/tac-gia/") < 0) {
//				dbc.updateData("insert into TblUrl values(N'" + url + "','')");
//				bw.write(url);
//				bw.newLine();
//			}
//		}
//		System.out.println("done");
//		bw.close();
		genPageId();
	}
	
	public static void genPageId() throws SQLException{
		UrlDAO urlDAO = new UrlDAO("Capstone");
		ResultSet rs = urlDAO.getData("select * from TblUrl order by id");
		while(rs.next()){
			urlDAO.updateData("update TblUrl set url_id = '"+getPageId(rs.getString(2))+"' where id ="+rs.getString(1));
		}
	}
	private static String getPageId(String url) {
		return url.substring(url.lastIndexOf("-") + 1, url.lastIndexOf("-") + 8);
	}
}
