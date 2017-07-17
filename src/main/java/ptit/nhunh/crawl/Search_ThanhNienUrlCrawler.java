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

public class Search_ThanhNienUrlCrawler {
	private String sourceUrl = "http://thanhnien.vn/search/Y2hpbmggc2FjaA==/chinh-sach/0-result-";
	private SQLDAO urlDAO;
	private ArrayList<Url> urls = new ArrayList<>();

	public static void main(String[] args) throws SQLException, IOException {
		new Search_ThanhNienUrlCrawler().getUrl();
	}

	public void getUrl() throws SQLException, IOException {
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.URL);

		ArrayList<Object> urls = this.urlDAO.getAll();

		for (int i = 76; i <= 99; i++) {
			System.out.println("--- Page " + i + " ---");
			String url = this.sourceUrl + i + ".html";
			Document doc = null;
			try {
				doc = Jsoup.connect(url).timeout(10000).get();
			} catch (IOException e) {
				System.out.println("--- Page " + i + " error to load ---");
				return;
			}

			Element div = doc.getElementById("tumblelog");

			Elements listArticle = div.getElementsByTag("article");

			for (int j = 0; j < listArticle.size(); j++) {
				Elements listA = listArticle.get(j).getElementsByTag("a");

				Url u = new Url();
				if (listA.get(0).attr("href").indexOf("http") < 0) {
					u.setUrl("http://thanhnien.vn" + listA.get(0).attr("href"));
				} else {
					u.setUrl(listA.get(0).attr("href"));
				}

				if (u.getUrl().indexOf("video") >= 0 || u.getUrl().indexOf("game") >= 0
						|| u.getUrl().indexOf("html") < 0) {
					continue;
				}

				u.setUrl_id(Utils.getThanhNienPageId(u.getUrl()));

				if (this.indexOf(u, this.urls) == -1) {
					Document subDoc = null;
					try {
						subDoc = Jsoup.connect(u.getUrl()).get();
					} catch (Exception e) {
						System.out.println("\t" + j + " error");
						continue;
					}

					// Get title
					Element h1 = subDoc.getElementsByClass("main-title").get(0);
					u.setTitles(h1.text().replaceAll("'", "\""));
					// End get title

					// Get comment count
					Elements subDiv = subDoc.getElementsByClass("comment-zone");
					if (subDiv.size() < 0) {
						subDiv = subDoc.getElementsByClass("comment-wrap");
					}
					if (subDiv.size() > 0) {
						Element h4 = subDiv.get(0).getElementsByTag("h4").get(0);
						Elements spans = h4.getElementsByTag("span");
						if (spans.size() > 0) {
							Element span = spans.get(0);

							u.setTotalComment(Integer.parseInt(
									span.text().substring(1, span.text().indexOf(" ")).trim()));
						}
					}
					// End get comment count

					// Get tag
					if (subDoc.getElementsByClass("tags").size() > 0) {
						Element ul = subDoc.getElementsByClass("tags").get(0);
						u.setTag(ul.text().replaceAll("'", "\""));
					}
					// End get tags

					this.urlDAO.insert(u);

				}
				System.out.println("\t" + j + " done");
			}
			System.out.println("--- Page " + i + " done ---");
		}
	}

	private int indexOf(Url u, ArrayList<Url> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getSource().equals("thanhnien")) {
				if (u.getUrl_id().trim().equals(list.get(i).getUrl_id().trim())) {
					return i;
				}
			}
		}
		return -1;
	}
}
