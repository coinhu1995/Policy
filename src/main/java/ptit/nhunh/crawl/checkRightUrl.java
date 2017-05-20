package ptit.nhunh.crawl;

import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.model.Url;

public class checkRightUrl {
	private static String[] title = { "chính sách", "quy định", "quyết định" };
	private static String[] tags = { "chính sách", "chỉ đạo", "ra lệnh", "chiến dịch",
			"đòi vỉa hè", "bộ nội vụ", "quyết định", "quy định", "đòi lại vỉa hè" };
	
	public static void main(String[] args) throws SQLException {
		new checkRightUrl().process();
	}
	
	private void process() throws SQLException {
		SQLDAO urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.URL);
		ArrayList<Object> urls = urlDAO.getData("select * from TblUrl where totalCmt > 0 order by id");
		for(int i = 0; i < urls.size(); i++){
			Url url = (Url) urls.get(i);
			if(this.checkTag(url.getTag())){
				urlDAO.update("update TblUrl set needed = 1 where id = "+url.getId(), UrlDAO.UPDATE_NEEDED);
			}
			
			if(this.checkTitles(url.getTitles())){
				urlDAO.update("update TblUrl set needed = 1 where id = "+url.getId(), UrlDAO.UPDATE_NEEDED);
			}
			
			System.out.println(url.getId() + " done!");
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
	
	private boolean checkTitles(String tit){
		for(int i = 0; i < title.length; i++){
			if(tit.contains(title[i])){
				return true;
			}
		}
		return false;
	}
}
