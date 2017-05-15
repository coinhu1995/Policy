package ptit.nhunh.crawl;

import java.sql.ResultSet;
import java.sql.SQLException;

import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.UrlDAO;

public class checkRightUrl {
	private static String[] failUrl = { "video", "tag", "error", "topic", "/sao/", "/nhac/",
			"/truyen-hinh/", "/phim/", "bong-da" };
	private static String[] title = { "chính sách", "quy định", "quyết định" };
	private static String[] tags = { "chính sách", "chỉ đạo", "ra lệnh", "chiến dịch",
			"đòi vỉa hè", "bộ nội vụ", "quyết định", "quy định", "đòi lại vỉa hè" };
	
	public static void main(String[] args) throws SQLException {
		new checkRightUrl().process();
	}
	
	private void process() throws SQLException {
		UrlDAO dbc = new UrlDAO("Capstone");
		ResultSet rs = dbc.getData("select * from TblUrl where totalCmt > 0 order by id");
		while(rs.next()){
			if(checkTag(rs.getString(10))){
				dbc.updateData("update TblUrl set needed = 1 where id = "+rs.getInt(1));
			}
			
			if(checkTitle(rs.getString(4))){
				dbc.updateData("update TblUrl set needed = 1 where id = "+rs.getInt(1));
			}
			
			System.out.println(rs.getInt(1) + " done!");
		}
	}
		
	
	private boolean checkTag(String tag){
		tag = tag.toLowerCase();
		for(int i = 0; i < tags.length; i++){
			if(tag.contains(tags[i])){
				return true;
			}
		}
		return false;
	}
	
	private boolean checkTitle(String tit){
		for(int i = 0; i < title.length; i++){
			if(tit.contains(title[i])){
				return true;
			}
		}
		return false;
	}
}
