package ptit.nhunh.crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.utils.Constants;
import ptit.nhunh.utils.Utils;

public class ArticleCrawl {
	private SQLDAO urlDao;
	private BufferedWriter bw;

	public ArticleCrawl() throws IOException {
		this.urlDao = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
		this.bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(Constants.LOG_PATH + "ArticleCrawelLog.txt"), true)));
		this.bw.write(Utils.getCurrentTime() + "\n ------------------------------\n");
	}

	public static void main(String[] args) throws SQLException, IOException {
		new ArticleCrawl().crawlVnExpressArticle();
	}

	public void crawlVnExpressArticle() throws SQLException, IOException {
		List<Object> urls = this.urlDao.getAll();
		String title = "";
		String content = "";
		String category = "";
		for (Object obj : urls) {
			Article url = (Article) obj;
			if (url.getSource().trim().equals("vnexpress.vn")) {
				try {
					Document html = Utils.getHtml(url.getUrl());
					Elements divs = html.getElementsByClass("fck_detail");
					if (divs.size() > 0) {
						title = html.getElementsByTag("h1").get(0).text();
						content = html.getElementsByClass("fck_detail").get(0).text();
						if (html.getElementsByClass("start").size() > 0) {
							category = html.getElementsByClass("start").get(0).text();
						}
						BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream(new File(Constants.DATA_PATH + "article\\"
										+ url.getUrl_id() + ".txt"))));

						bw2.write("<category>" + category + "</category>");
						bw2.newLine();
						bw2.write("<title>" + title + "</title>");
						bw2.newLine();
						bw2.write("<content>" + content + "</content>");

						bw2.close();
					}
					System.out.println(url.getId() + " done!");
				} catch (IOException e) {
					this.bw.write(String.format("%-10s", url.getId()) + "\t" + e.getMessage() + "\t"
							+ url.getUrl());
				}
			}
		}
	}
}
