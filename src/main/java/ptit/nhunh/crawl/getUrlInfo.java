package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.model.ResponseObject;
import ptit.nhunh.model.Url;
import ptit.nhunh.tool.ConvertJson2Java;
import ptit.nhunh.utils.Utils;

public class getUrlInfo {
	private UrlDAO urlDAO = new UrlDAO("Capstone");
	private String urlComment = "http://usi.saas.vnexpress.net/index/"
			+ "get?offset=0&limit=500&objecttype=1&siteid=1000000&objectid=";

	public static void main(String[] args) throws SQLException, IOException {
		new Thread(){
			@Override
			public void run() {
				try {
					new getUrlInfo().process();
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
//		new Thread(){
//			@Override
//			public void run() {
//				try {
//					new getUrlInfo().process(17001, 33608);
//				} catch (SQLException | IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
	}

	private void process() throws SQLException, IOException {
		ArrayList<Url> listURL = this.urlDAO.getAllUrl();
		for (Url u : listURL) {
			try {
				Document doc = Utils.getHtml(u.getUrl());

				// get tittle
				u.setTittle(doc.getElementsByTag("title").get(0).text());

				// get total number comment
				try {
					Document doc2 = Jsoup.connect(this.urlComment + u.getUrl_id())
							.ignoreContentType(true).get();
					String s = doc2.getElementsByTag("body").get(0).text();
					s = s.replaceAll("\"Nhân dân tệ\"", "Nhân dân tệ");
					System.out.println(s);
					ConvertJson2Java cvt = new ConvertJson2Java();
					ResponseObject data = cvt.convert(s);

					u.setTotalParComment(Integer.parseInt(data.getData().getTotal()));
					u.setTotalComment(Integer.parseInt(data.getData().getTotalitem()));
				} catch (Exception e1) {
					System.out.println("crawl number comment. url: " + u.getUrl());
					e1.printStackTrace();
				}

				// get tags
				Elements list = doc.getElementsByClass("block_tag");
				String s = list.text();
				u.setTag(s);

				// update to DB
				String sql = "update TblUrl set title = N'" + u.getTittle().replaceAll("'", "\"")
						+ "', totalCmt = " + u.getTotalComment() + ",totalParCmt = "
						+ u.getTotalParComment() + " ,tag = N'" + u.getTag().replaceAll("'", "\"")
						+ "' where id = " + u.getId();
				this.urlDAO.updateData(sql);

				System.out.println(u.getId() + " done!");
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
				System.out.println(u.getUrl());
			}
		}
	}
}
