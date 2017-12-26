package ptit.nhunh.crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Item;
import ptit.nhunh.model.ResponseObject;
import ptit.nhunh.tool.ConvertJson2Java;;

public class VnExpressCommentCrawler {
	private static String urlComment = "http://usi.saas.vnexpress.net/index/"
			+ "get?offset=0&limit=1000&objecttype=1&siteid=1000000&objectid=";
	private static String urlReply = "http://usi.saas.vnexpress.net/index/getreplay?"
			+ "siteid=1000000&objecttype=1&limit=500&offset=0&";
	private SQLDAO cmtDAO;
	private SQLDAO urlDAO;
	private BufferedWriter bw;

	public static void main(String[] args) throws SQLException, IOException {
		new VnExpressCommentCrawler().process();
	}

	public VnExpressCommentCrawler() {
		this.cmtDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
	}

	public void process() throws SQLException, IOException {
		this.bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("CrawlCommentLogFile.txt"))));
		System.out.println("Crawling...");

		// this.dbConnect.updateData("delete from TblCmt");
		// this.dbConnect.updateData("DBCC CHECKIDENT ('TblCmt', RESEED, 0)");

		ArrayList<Object> listURL = this.urlDAO
				.getData("select * from TblUrl where source = 'vnexpress.vn' order by id");

		for (Object o : listURL) {
			Article u = (Article) o;
			this.crawlParentComment(u.getUrl_id(), u.getId());
			System.out.println(u.getId() + " done!");
		}

		this.bw.close();
	}

	private void crawlParentComment(String pageId, int id) {
		Document doc = null;
		try {
			doc = Jsoup.connect(urlComment + pageId).ignoreContentType(true).get();
			String s = doc.getElementsByTag("body").get(0).text();
			ConvertJson2Java cvt = new ConvertJson2Java();
			ResponseObject data = null;

			try {
				if (pageId.equals("3174878"))
					s = s.replaceAll("\"Nhân dân tệ\"", "Nhân dân tệ");
				data = cvt.convert(s);
			} catch (Exception e) {
				System.out.println(pageId);
				System.out.println(s);
				System.out.println("Loi convert json: " + e.getMessage() + " " + pageId);
				return;
			}

			for (int i = 0; i < Integer.parseInt(data.getData().getTotal()); i++) {
				Item it = data.getData().getItem()[i];
				Comment c = it.convert2Comment();
				c.setContent(c.getContent().replaceAll("'", ""));
				c.setFullname(c.getFullname().replaceAll("'", ""));
				c.setArticleid(id);
				try {
					this.cmtDAO.insert(c);
				} catch (SQLException e) {
					try {
						this.bw.write("crawl parent comment: " + e.getMessage() + " " + pageId);
						this.bw.newLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			try {
				this.bw.write(e1.getMessage() + " " + pageId);
				this.bw.newLine();
			} catch (IOException e) {
				System.out.println("Loi ghi file: " + e.getMessage() + " " + pageId);
			}
			e1.printStackTrace();
			return;
		}
	}

	private void crawlReplyComment(String pageId, String commentId) {
		String urlRep = urlReply + "objectid=" + pageId + "&id=" + commentId;
		Document doc = null;
		try {
			doc = Jsoup.connect(urlRep).ignoreContentType(true).timeout(1000000).get();
			String s = doc.getElementsByTag("body").get(0).text();
			ConvertJson2Java cvt = new ConvertJson2Java();
			ResponseObject data = cvt.convert(s);
			for (int i = 0; i < data.getData().getItem().length; i++) {
				Item it = data.getData().getItem()[i];
				Comment c = it.convert2Comment();
				c.setContent(c.getContent().replaceAll("'", ""));
				c.setFullname(c.getFullname().replaceAll("'", ""));

				try {
					this.cmtDAO.insert(c);
				} catch (SQLException e) {
					this.bw.write("crawl reply comment: " + e.getMessage() + " " + pageId);
					this.bw.newLine();
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
