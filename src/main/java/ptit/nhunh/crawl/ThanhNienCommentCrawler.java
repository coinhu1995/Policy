package ptit.nhunh.crawl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.model.Comment;
import ptit.nhunh.utils.Constants;
import ptit.nhunh.utils.Utils;


/**
 * real-id: is another id of page to get comment
 * 
 * @author coinh
 *
 */
public class ThanhNienCommentCrawler {
	private SQLDAO cmtDAO;

	public static void main(String[] args) throws SQLException, IOException {
		ArrayList<Object> listUrl = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE)
				.getData("select * from TblUrl where source = 'thanhnien' order by id");
		new ThanhNienCommentCrawler().process(listUrl);
		new ThanhNienCommentCrawler().processErrorUrl();
	}

	public ThanhNienCommentCrawler() {
		this.cmtDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
	}

	private void process(ArrayList<Object> listUrl) throws SQLException, IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"D:\\NHU\\WORKSPACE\\Capstone\\Policy\\src\\main\\resources\\log\\logCrawlThanhNien.txt"));

		for (int i = 0; i < listUrl.size(); i++) {
			Article url = (Article) listUrl.get(i);
			Document doc = null;
			try {
				doc = Utils.getHtml(url.getUrl());
				String realID = "";
				// Get real-id of page
				try {
					Element link = doc.getElementById("hdCurrentContent");
					realID = link.attr("value");
				} catch (Exception e) {
					bw.write(url.getUrl() + "\n");
					bw.write(e.getMessage() + "\n\n");
					continue;
				}
				// End get real-id of page

				// Crawl comment
				boolean stop = false;
				int page = 1;
				while (stop == false) {
					String u = Constants.TNURL + realID + "-" + page + "-like.html";
					Document docCmt = null;
					try {
						docCmt = Utils.getHtml(u);
					} catch (Exception e) {
						bw.write(u + "\n");
						bw.write(e.getMessage() + "\n\n");
						continue;
					}

					Elements listComment = null;

					try {
						listComment = docCmt.getElementsByClass("comment-article");
					} catch (Exception e) {
						bw.write(u + "\n");
						bw.write(e.getMessage() + "\n\n");
						continue;
					}
					if (listComment.size() > 0) {
						for (int j = 0; j < listComment.size(); j++) {
							Element thisElm = listComment.get(j);
							Comment c = new Comment();

							Elements h4 = thisElm.getElementsByTag("h4");

							c.setFullname(h4.get(0).text().replaceAll("'", "\""));
							c.setTime(thisElm.getElementsByTag("time").get(0).text().replaceAll("'",
									"\""));
							c.setContent(thisElm.getElementsByClass("Comments-item-Content").get(0)
									.text().replaceAll("'", "\""));
							c.setUserlike(Integer.parseInt(thisElm.getElementsByTag("span").get(0)
									.text().replaceAll("'", "\"")));
							c.setCmt_id("tn" + listComment.get(j).attr("rel"));
							c.setParent_id("tn" + listComment.get(j).attr("rel"));
							c.setPage_id("tn" + url.getUrl_id());
							c.setArticleid(url.getId());
							this.cmtDAO.insert(c);
						}
					} else {
						stop = true;
					}
					page++;
				}
				// End crawl comment
				System.out.println(url.getUrl_id() + " " + url.getId() + " done");
			} catch (Exception e) {
				bw.write(url.getUrl() + "\n");
				bw.write(e.getMessage() + "\n\n");
				continue;
			}

			
		}
		bw.close();
		System.out.println("DONE");
	}

	/**
	 * khi crawl comment co nhung url bi request timeout thi se duoc crawl lai
	 * trong ham nay
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	private void processErrorUrl() throws IOException, SQLException {
		System.out.println("\n\nProcess log file\n\n");
		BufferedReader br = new BufferedReader(new FileReader(
				"D:\\NHU\\WORKSPACE\\Capstone\\Policy\\src\\main\\resources\\log\\logCrawlThanhNien.txt"));
		ArrayList<Object> listUrl = new ArrayList<>();
		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.indexOf("http") >= 0) {
				Article u = new Article();
				u.setUrl(line);
				u.setUrl_id(Utils.getThanhNienPageId(line));
				listUrl.add(u);
			}
		}

		br.close();

		this.process(listUrl);
	}
}
