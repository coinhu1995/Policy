package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.model.Url;
import ptit.nhunh.utils.Utils;

public class SearchThanhNienUrl {
	private String sourceUrl = "http://thanhnien.vn/search/Y2hpbmggc2FjaA==/chinh-sach/0-result-";
	private UrlDAO urlDAO;
	private ArrayList<Url> urls = new ArrayList<>();

	public static void main(String[] args) throws SQLException, IOException {
		new SearchThanhNienUrl().getUrl();
	}	

	public void getUrl() throws SQLException, IOException {

		this.urlDAO = new UrlDAO("Capstone");

		ResultSet rs = this.urlDAO.getData("select * from TblUrl order by id");

		while (rs.next()) {
			Url u = new Url(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getString(9));
			this.urls.add(u);
		}

		for (int i = 41; i <= 99; i++) {
			System.out.println("--- Page " + i + " ---");
			String url = this.sourceUrl + i + ".html";
			Document doc = null;
			try {
				doc = Jsoup.connect(url).timeout(5000).get();
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

				if (indexOf(u, urls) == -1) {
					Document subDoc = null;
					try {
						subDoc = Jsoup.connect(u.getUrl()).get();
					} catch (Exception e) {
						System.out.println("\t" + j + " error");
						continue;
					}

					// Get title
					Element h1 = subDoc.getElementsByClass("main-title").get(0);
					u.setTittle(h1.text().replaceAll("'", "\""));
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

					urlDAO.updateData("insert into TblUrl values(N'" + u.getUrl() + "','"
							+ u.getUrl_id() + "',N'" + u.getTittle() + "', 0 ,'thanhnien', "
							+ u.getTotalComment() + " , 0 ,N'" + u.getTag() + "') ");

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
