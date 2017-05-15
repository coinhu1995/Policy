package ptit.nhunh.crawl;

import java.io.BufferedWriter;
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

public class AllVnExpressUrl {
	private static String url1 = "http://giadinh.vnexpress.net/tin-tuc/tieu-dung/toi-da-tri-thoi-tieu-hoang-cua-vo-nhu-the-nao-3269548.html";
	private static ArrayList<String> urls;

	private static BufferedWriter bw;
	private static int dem = 0;
	private static UrlDAO urlDAO;
	private static ResultSet rs;

	private static String[] failUrl = { "video", "tag", "error", "topic", "/sao/", "/nhac/",
			"/truyen-hinh/", "/phim/", "news" };

	public static void main(String[] args) throws IOException, SQLException {
		urlDAO = new UrlDAO("Capstone");
		urls = new ArrayList<>();
		ResultSet rsUrlDB = urlDAO.getData("select * from TblUrl order by id");

		while (rsUrlDB.next()) {
			urls.add(rsUrlDB.getString(2));
		}

		System.out.println("Collecting URL...");
		urls.add(url1);

		getLinks(urls.size() - 1);
		System.out.println("Number of URL: " + urls.size());
		System.out.println("Done...");
	}

	public static void getLinks(int i) throws SQLException {
		Document doc = null;
		try {
			doc = Jsoup.connect(urls.get(i)).timeout(100000).get();
		} catch (IOException e) {
			System.out.println(urls.get(i));
			e.printStackTrace();
		}
		if (doc != null) {
			Elements elements = doc.select("a");
			for (Element element : elements) {
				String url2 = element.absUrl("href");
				if (!urls.contains(url2)) {
					if (url2.indexOf("vnexpress") >= 0 && url2.indexOf("http") >= 0
							&& url2.indexOf("html") >= 0 && url2.charAt(url2.length() - 6) < '9'
							&& url2.charAt(url2.length() - 6) > '0' && checkRightUrl(url2)) {
						rs = urlDAO.getData("select * from TblUrl where url = '" + url2 + "'");
						if (rs.next() == false) {
							urlDAO.updateData("insert into TblUrl values(N'" + url2 + "','"
									+ getPageId(url2) + "','','','vnexpress.vn', '04042017' )");
							urls.add(url2);
							System.out.println(++dem);
						}
					}
				}
			}
		}
		getLinks(i + 1);
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

	private static boolean checkRightUrl(String url) {
		if (url.charAt(url.length() - 7) == 'p')
			return false;
		for (int i = 0; i < failUrl.length - 1; i++) {
			if (url.indexOf(failUrl[i]) >= 0) {
				return false;
			}
		}
		return true;
	}
}
