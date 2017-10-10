package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.model.ResponseObject;
import ptit.nhunh.tool.ConvertJson2Java;
import ptit.nhunh.utils.Utils;

public class getUrlInfo {
	private SQLDAO articleDAO;
	private String urlComment = "http://usi.saas.vnexpress.net/index/"
			+ "get?offset=0&limit=500&objecttype=1&siteid=1000000&objectid=";

	public static void main(String[] args) throws SQLException, IOException {
		try {
			new getUrlInfo().process(1, 200);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	public getUrlInfo() {
		this.articleDAO = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
	}

	private void process(int start, int end) throws SQLException, IOException {
		ArrayList<Object> listURL = this.articleDAO.getData("select * from TblArticle where id >= 286 order by id");
		// ArrayList<Object> listURL = this.articleDAO.getAll();
		for (Object o : listURL) {
			Article url = (Article) o;
			try {
				Document doc = Utils.getHtml(url.getUrl());

				// get tittle
				url.setTitle(doc.getElementsByTag("title").get(0).text().replaceAll("'", "\""));

				// get total number comment
				String origin = "", wrapper = "";
				try {
					Document doc2 = Utils.getHtml(this.urlComment + url.getUrl_id());
					origin = doc2.getElementsByTag("body").get(0).text();
					wrapper = origin.replaceAll("'", "");
					wrapper = wrapper.replaceAll("Thành \"Nhân dân tệ\"", "Vô thường");
					if (wrapper.indexOf("total") < 0) {
						System.out.println(url.getId() + " khong co!");
						continue;
					}
					ConvertJson2Java cvt = new ConvertJson2Java();
					ResponseObject data = cvt.convert(wrapper);
					url.setTotalParComment(Integer.parseInt(data.getData().getTotal()));
					url.setTotalComment(Integer.parseInt(data.getData().getTotalitem()));
				} catch (Exception e1) {
					System.out.println(origin);
					System.out.println(wrapper);
					System.out.println("crawl number comment. url: " + url.getUrl());
					throw e1;
				}

				// get tags
				Elements list = doc.getElementsByClass("block_tag");
				url.setTag(list.text().replaceAll("'", "\""));

				this.articleDAO.update(url);

				System.out.println(url.getId() + " done!");
			} catch (Exception e2) {
				System.out.println(url.getUrl());
				throw e2;
			}
		}
	}
}
