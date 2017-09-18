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
import ptit.nhunh.model.Article;
import ptit.nhunh.utils.Utils;

public class VnExpressUrlCrawler {
	private String baseUrl = "http://giadinh.vnexpress.net/tin-tuc/tieu-dung/toi-da-tri-thoi-tieu-hoang-cua-vo-nhu-the-nao-3269548.html";
	private String[] failUrl = { "video", "tag", "error", "topic", "/sao/", "/nhac/",
			"/truyen-hinh/", "/phim/", "news" };

	private ArrayList<Object> urls;
	private Article url;
	private int dem = 0;
	private SQLDAO urlDAO;

	public VnExpressUrlCrawler() {
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
		this.url = new Article();
	}

	public void process() throws IOException, SQLException {
		this.urls = this.urlDAO.getAll();
		System.out.println("Collecting URL...");

		Article u = new Article();
		u.setUrl(this.baseUrl);
		this.urls.add(u);

		this.getLinks(this.urls.size() - 1);
		System.out.println("Number of URL: " + this.urls.size());
		System.out.println("Done...");
	}

	public void getLinks(int i) throws SQLException {
		Document doc = null;
		try {
			Article u = (Article) this.urls.get(i);
			doc = Jsoup.connect(u.getUrl()).timeout(100000).get();
		} catch (IOException e) {
			System.out.println(this.urls.get(i));
			e.printStackTrace();
		}
		if (doc != null) {
			Elements elements = doc.select("a");
			for (Element element : elements) {
				this.url.setUrl(element.attr("href"));
				this.url.setUrl_id(Utils.getVnExpressPageId(this.url.getUrl()));
				if (Utils.contain(this.urls, this.url) == -1) {
					if (this.url.getUrl().indexOf("vnexpress") >= 0
							&& this.url.getUrl().indexOf("http") >= 0
							&& this.url.getUrl().indexOf("html") >= 0
							&& this.url.getUrl().charAt(this.url.getUrl().length() - 6) < '9'
							&& this.url.getUrl().charAt(this.url.getUrl().length() - 6) > '0'
							&& this.checkRightUrl(this.url.getUrl())) {
						this.urlDAO.insert(this.url);
						this.urls.add(this.url);
						System.out.println(++this.dem);
					}
				}
			}
		}
		this.getLinks(i + 1);
	}

	private boolean checkRightUrl(String url) {
		if (url.charAt(url.length() - 7) == 'p')
			return false;
		for (int i = 0; i < this.failUrl.length - 1; i++) {
			if (url.indexOf(this.failUrl[i]) >= 0) {
				return false;
			}
		}
		return true;
	}
}
