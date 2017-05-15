package ptit.nhunh.crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.UrlDAO;

public class GetAllUrl {
	// http://vnexpress.net/tin-tuc/phap-luat/thoat-toi-giet-nguoi-do-tron-ky-suot-23-nam-3515824.html
	private static String url1 = "http://ione.vnexpress.net/tin-tuc/lam-dep/makeup/10-meo-giup-mat-dep-rang-ro-khong-can-makeup-3458975.html";
	private static ArrayList<String> urls;
	private static BufferedWriter bw;
	private static int dem = 0;
	private static UrlDAO urlDAO;
	private static ResultSet rs;
	private static String[] failUrl = {"video" ,"tag", "error", "topic", "/sao/", "/nhac/", "/truyen-hinh/",
			"/phim/"};

	public static void main(String[] args) throws IOException, SQLException {
		urlDAO = new UrlDAO("Capstone");
		// dbc.updateData("delete from TblUrl DBCC CHECKIDENT ('TblUrl', RESEED,
		// 0)");
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("url.txt"))));
		urls = new ArrayList<>();
		System.out.println("Collecting URL...");
		urls.add(url1);
		urlDAO.updateData("insert into TblUrl values(N'" + urls.get(0) + "','" + getPageId(url1)
				+ "',N'Quận 1 sẽ xử lý lấn chiếm vỉa hè mạnh tay hơn')");
		getLinks(0);
		System.out.println("Number of URL: " + urls.size());
		System.out.println("Done...");
		bw.close();
	}

	public static void getLinks(int i) throws SQLException {
		Document doc = null;
		try {
			doc = Jsoup.connect(urls.get(i)).get();
		} catch (IOException e) {
			System.out.println(urls.get(i));
			e.printStackTrace();
			return;
		}
		Elements elements = doc.select("a");
		for (Element element : elements) {
			String url2 = element.absUrl("href");
			if (url2.indexOf("vnexpress") >= 0 && url2.indexOf("http") >= 0
					&& url2.indexOf("html") >= 0 && url2.charAt(url2.length() - 6) < '9'
					&& url2.charAt(url2.length() - 6) > '0' && checkFailUrl(url2)) {
				rs = urlDAO.getData("select * from TblUrl where url = '" + url2 + "'");
				if (rs.next() == false) {
					urlDAO.updateData("insert into TblUrl values(N'" + url2 + "','" + getPageId(url2)
							+ "','')");
					urls.add(url2);
					System.out.println(++dem);
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
	
	private static boolean checkFailUrl(String url){
		if(url.charAt(url.length() - 7) == 'p')
			return false;
		for(int i = 0; i < failUrl.length - 1; i++){
			if(url.indexOf(failUrl[i]) >= 0){
				return false;
			}
		}
		return true;
	}
}
