package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Url;
import ptit.nhunh.utils.Utils;

/**
 * Get url theo cách tìm kiếm từ khóa chính sách của vnexpress
 * 
 * @author uhn
 *
 */
public class SearchVnExpressUrlCrawler {
	private String sourceUrl = "http://timkiem.vnexpress.net/?q=ch%C3%ADnh%20s%C3%A1ch&media_type=all&fromdate=0&todate=0&latest=&cate_code=&search_f=title,tag_list&date_format=all&page=";
	private SQLDAO urlDAO;
	private ArrayList<Object> urls = new ArrayList<>();

	public static void main(String[] args) throws SQLException {
		new SearchVnExpressUrlCrawler().getUrl();
	}

	public void getUrl() throws SQLException {
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.URL);

		this.urls = this.urlDAO.getAll();

//		this.dbc.updateData("delete from TblUrl");
//		this.dbc.updateData("DBCC CHECKIDENT ('TblUrl', RESEED, 0)");
		for (int i = 1; i <= 37; i++) {
			System.out.println(i);
			String url = this.sourceUrl + i;
			Document doc = null;
			try {
				doc = Jsoup.connect(url).timeout(100000).get();
				Element ul = doc.getElementById("news_home");
				Elements list_li = ul.getElementsByTag("li");
				for (int j = 0; j < list_li.size(); j++) {
					Elements list_a = list_li.get(j).getElementsByTag("a");
					Url u = new Url();
					u.setUrl(list_a.get(0).attr("href").replace("?utm_source=search_vne", ""));
					u.setSource("vnexpress.vn");
					u.setUrl_id(Utils.getVnExpressPageId(u.getUrl()));
					if (Utils.contain(this.urls, u) == -1) {
						this.urlDAO.insert(u);
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage() + url);
			}
		}
	}

	private static String getPageId(String url) {
		try {
			return url.substring(url.lastIndexOf("-") + 1, url.lastIndexOf("-") + 8);
		} catch (Exception e) {
			System.out.println(url);
			e.printStackTrace();
			return "fail";
		}
	}
}
