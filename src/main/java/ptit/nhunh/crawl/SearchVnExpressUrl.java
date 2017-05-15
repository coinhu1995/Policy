package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.UrlDAO;

/**
 * Get url theo cách tìm kiếm từ khóa chính sách của vnexpress
 * 
 * @author uhn
 *
 */
public class SearchVnExpressUrl {
	private String sourceUrl = "http://timkiem.vnexpress.net/?q=ch%C3%ADnh%20s%C3%A1ch&media_type=all&fromdate=0&todate=0&latest=&cate_code=&search_f=title,tag_list&date_format=all&page=";
	private UrlDAO dbc;
	private ArrayList<String> urls = new ArrayList<>();

	public static void main(String[] args) throws SQLException {
		new SearchVnExpressUrl().getUrl();
	}

	public void getUrl() throws SQLException {
		this.dbc = new UrlDAO("Capstone");

		ResultSet rs = this.dbc.getData("select * from TblUrl order by id");

		while (rs.next()) {
			this.urls.add(rs.getString(2));
		}

//		this.dbc.updateData("delete from TblUrl");
//		this.dbc.updateData("DBCC CHECKIDENT ('TblUrl', RESEED, 0)");
		for (int i = 1; i <= 37; i++) {
			System.out.println(i);
			String url = this.sourceUrl + i;
			Document doc = null;
			try {
				doc = Jsoup.connect(url).timeout(100000).get();
			} catch (IOException e) {
				System.out.println(e.getMessage() + url);
				return;
			}

			Element ul = doc.getElementById("news_home");
			Elements list_li = ul.getElementsByTag("li");
			for (int j = 0; j < list_li.size(); j++) {
				Elements list_a = list_li.get(j).getElementsByTag("a");
				String url2 = list_a.get(0).attr("href").replace("?utm_source=search_vne", "");
				
				if (!this.urls.contains(url2)) {
					String sql = "insert into TblUrl values('" + url2 + "', '" + getPageId(url2)
							+ "', '0', 0, 'vnexpress.vn', '06042017', 0, 0, '');";
					this.dbc.updateData(sql);
				}
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
