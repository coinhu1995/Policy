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
//				Document html = Utils.getHtml(url.getUrl());
//				try {
//					Elements divs = html.getElementsByClass("sidebar_1");
//					if (divs.size() > 0) {
//						title = html.getElementsByTag("h1").get(0).text();
//						url.setTitle(title.trim());
//						content = divs.toString();
//
//						Elements imgs = divs.get(0).getElementsByTag("img");
//						for (int i = 0; i < imgs.size(); i++) {
//							Element e = imgs.get(i);
//							content = content.replace(imgs.get(i).toString(), imgs.get(i).toString() + "</img>");
//						}
//						Elements brs = divs.get(0).getElementsByTag("br");
//						content = content.replace("</br>", "");
//						content = content.replace("<br>", "<br></br>");
//						// for (int i = 0; i < brs.size(); i++) {
//						// if (brs.get(i).toString().indexOf("</br>") < 0) {
//						// content = content.replace(brs.get(i).toString(), brs.get(i).toString() +
//						// "</br>");
//						// break;
//						// }
//						// }
//						if (html.getElementsByClass("start").size() > 0) {
//							category = html.getElementsByClass("start").get(0).text().trim();
//							url.setCategory(category);
//						}
//						File folder = new File(Constants.VNEXPRESS_ARTICLE_PATH + url.getUrl_id());
//
//						if (!folder.exists()) {
//							folder.mkdirs();
//						}
//
//						File f_css = new File(Constants.CSS + url.getUrl_id() + ".css");
//						BufferedWriter bw_css = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f_css)));
//						Elements styles = html.getElementsByTag("style");
//
//						for (int i = 0; i < styles.size(); i++) {
//							bw_css.write(styles.get(i).toString());
//						}
//
//						File f = new File(
//								Constants.VNEXPRESS_ARTICLE_PATH + url.getUrl_id() + "\\" + url.getUrl_id() + ".xhtml");
//						f.createNewFile();
//						BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
//
//						content = content.replace("href=\"javascript:;\"", "");
//						content = content.replace("data-href", "href");
//
//						bw2.write(Constants.PREFIX_CONTENT);
//						bw2.write(content);
//						bw2.write(Constants.SUFFIX_CONTENT);
//
//						url.setContentFilePath(f.getPath().replace("src\\main\\webapp\\", ""));
//						if (divs.get(0).getElementsByTag("img").size() > 0) {
//							url.setImageUrl(divs.get(0).getElementsByTag("img").get(0).attr("src"));
//						} else {
//							url.setImageUrl(
//									"https://s.vnecdn.net/vnexpress/restruct/i/v46/graphics/img_logo_vne_web.gif");
//						}
//						this.urlDao.update(url);
//						bw_css.close();
//						bw2.close();
//					}
//					System.out.println(url.getId() + " done!");
//				} catch (IOException e) {
//					e.printStackTrace();
//					this.bw.write(String.format("%-10s", url.getId()) + "\t" + e.getMessage() + "\t" + url.getUrl());
//				}
			} else {
				Document html = Utils.getHtml(url.getUrl());
				try {
					Elements divs = html.getElementsByClass("content");
					title = html.getElementsByTag("h1").get(0).text();
					if (divs.size() > 0) {
						url.setTitle(title.trim());
						content = divs.toString();
						Elements imgs = divs.get(0).getElementsByTag("img");
						for (int i = 0; i < imgs.size(); i++) {
							content = content.replace(imgs.get(i).toString(), imgs.get(i).toString() + "</img>");
						}
						Elements brs = divs.get(0).getElementsByTag("br");
						content = content.replace("</br>", "");
						for (int i = 0; i < brs.size(); i++) {
							if (brs.get(i).attributes().size() > 0) {
								content = content.replace(brs.get(i).toString(), brs.get(i).toString() + "</br>");
								break;
							}
						}
						content = content.replace("<br>", "<br></br>");
						File f = new File(Constants.THANHNIEN_ARTICLE_PATH + url.getUrl_id() + ".xhtml");
						f.createNewFile();
						BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));

						content = content.replace("<a href=\"/", "<a href=\"/http://thanhnien.vn/");

						bw2.write(Constants.PREFIX_CONTENT);
						bw2.write(content);
						bw2.write(Constants.SUFFIX_CONTENT);

						url.setContentFilePath(f.getPath().replace("src\\main\\webapp\\", ""));
						if (divs.get(0).getElementsByTag("img").size() > 0) {
							url.setImageUrl(divs.get(0).getElementsByTag("img").get(0).attr("src"));
						} else {
							url.setImageUrl("http://image.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png");
						}
						this.urlDao.update(url);
						bw2.close();
					}
					System.out.println(url.getId() + " done!");
				} catch (IOException e) {
					e.printStackTrace();
					this.bw.write(String.format("%-10s", url.getId()) + "\t" + e.getMessage() + "\t" + url.getUrl());
				}
			}
		}
	}
}
