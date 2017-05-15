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

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Url;
import ptit.nhunh.utils.Constants;
import ptit.nhunh.utils.Utils;

public class CrawlCommentThanhNien {
	private CommentDAO commentDAO;

	public static void main(String[] args) throws SQLException, IOException {
		ArrayList<Url> listUrl = new UrlDAO("Capstone").getThanhNienUrl();
		new CrawlCommentThanhNien().process(listUrl);
		new CrawlCommentThanhNien().processErrorUrl();
	}

	public CrawlCommentThanhNien() {
		this.commentDAO = new CommentDAO("Capstone");
	}

	private void process(ArrayList<Url> listUrl) throws SQLException, IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"D:\\NHU\\WORKSPACE\\Capstone\\Policy\\src\\main\\resource\\log\\logCrawlThanhNien.txt"));

		for (int i = 0; i < listUrl.size(); i++) {
			// Check if url was processed
			// ResultSet rs = this.commentDAO.getData("select * from
			// TblComment where ");
			// Check if url was processed
			Document doc = null;
			try {
				doc = Utils.getHtml(listUrl.get(i).getUrl());
			} catch (Exception e) {
				bw.write(listUrl.get(i).getUrl() + "\n");
				bw.write(e.getMessage() + "\n\n");
				continue;
			}

			String realID = "";
			// Get real id of page
			try {
				Element link = doc.getElementById("hdCurrentContent");
				realID = link.attr("value");
			} catch (Exception e) {
				bw.write(listUrl.get(i).getUrl() + "\n");
				bw.write(e.getMessage() + "\n\n");
				continue;
			}
			// End get real id of page

			// Crawl comment
			boolean stop = false;
			int page = 1;
			while (stop == false) {
				String url = Constants.TNURL + realID + "-" + page + "-like.html";
				Document docCmt = null;
				try {
					docCmt = Utils.getHtml(url);
				} catch (Exception e) {
					bw.write(listUrl.get(i).getUrl() + "\n");
					bw.write(e.getMessage() + "\n\n");
					continue;
				}

				Elements listComment = null;

				try {
					listComment = docCmt.getElementsByClass("comment-article");
				} catch (Exception e) {
					bw.write(listUrl.get(i).getUrl() + "\n");
					bw.write(e.getMessage() + "\n\n");
					continue;
				}
				if (listComment.size() > 0) {
					for (int j = 0; j < listComment.size(); j++) {
						Element thisElm = listComment.get(j);
						Comment c = new Comment();

						Elements h4 = thisElm.getElementsByTag("h4");

						// Name
						c.setFullname(h4.get(0).text().replaceAll("'", "\""));
						// Name

						// Time
						c.setTime(thisElm.getElementsByTag("time").get(0).text().replaceAll("'",
								"\""));
						// Time

						// Content
						c.setContent(thisElm.getElementsByClass("Comments-item-Content").get(0)
								.text().replaceAll("'", "\""));
						// Content

						// Like count
						c.setUserlike(Integer.parseInt(thisElm.getElementsByTag("span").get(0)
								.text().replaceAll("'", "\"")));
						// Like count

						// Comment ID
						c.setCmt_id("tn" + listComment.get(j).attr("rel"));
						c.setParent_id("tn" + listComment.get(j).attr("rel"));
						// Comment ID

						// PAGE ID
						c.setPage_id("tn" + listUrl.get(i).getUrl_id());
						// PAGE ID

						this.commentDAO.insertCmt(c);
					}
				} else {
					stop = true;
				}
				page++;
			}
			// End crawl comment
			System.out.println(listUrl.get(i).getUrl_id() + " " + listUrl.get(i).getId() + " done");
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
				"D:\\NHU\\WORKSPACE\\Capstone\\Policy\\src\\main\\resource\\log\\logCrawlThanhNien.txt"));
		ArrayList<Url> listUrl = new ArrayList<>();
		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.indexOf("http") >= 0) {
				Url u = new Url();
				u.setUrl(line);
				u.setUrl_id(Utils.getThanhNienPageId(line));
				listUrl.add(u);
			}
		}

		br.close();

		this.process(listUrl);
	}
}
