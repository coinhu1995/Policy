package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.model.ResponseObject;
import ptit.nhunh.model.Url;
import ptit.nhunh.tool.ConvertJson2Java;
import ptit.nhunh.utils.Utils;

public class getUrlInfo {
	private SQLDAO urlDAO;
	private String urlComment = "http://usi.saas.vnexpress.net/index/"
			+ "get?offset=0&limit=500&objecttype=1&siteid=1000000&objectid=";

	public static void main(String[] args) throws SQLException, IOException {
		new Thread() {
			@Override
			public void run() {
				try {
					new getUrlInfo().process(1, 17000);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				try {
					new getUrlInfo().process(17000, 33608);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public getUrlInfo() {
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.URL);
	}

	private void process(int start, int end) throws SQLException, IOException {
		ArrayList<Object> listURL = this.urlDAO.getData(
				"select * from TblUrl where id >= " + start + " and id < " + end + "order by id");
		for (Object o : listURL) {
			Url url = (Url) o;
			try {
				Document doc = Utils.getHtml(url.getUrl());

				// get tittle
				url.setTitles(doc.getElementsByTag("title").get(0).text().replaceAll("'", "\""));

				// get total number comment
				try {
					Document doc2 = Jsoup.connect(this.urlComment + url.getUrl_id())
							.ignoreContentType(true).get();
					String s = doc2.getElementsByTag("body").get(0).text();
					s = s.replaceAll("\"Nhân dân tệ\"", "Nhân dân tệ");
					System.out.println(s);
					ConvertJson2Java cvt = new ConvertJson2Java();
					ResponseObject data = cvt.convert(s);

					url.setTotalParComment(Integer.parseInt(data.getData().getTotal()));
					url.setTotalComment(Integer.parseInt(data.getData().getTotalitem()));
				} catch (Exception e1) {
					System.out.println("crawl number comment. url: " + url.getUrl());
					e1.printStackTrace();
				}

				// get tags
				Elements list = doc.getElementsByClass("block_tag");
				url.setTag(list.text().replaceAll("'", "\""));

				this.urlDAO.update(url, UrlDAO.UPDATE_TITLES_AND_TAGS);

				System.out.println(url.getId() + " done!");
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
				System.out.println(url.getUrl());
			}
		}
	}
}
