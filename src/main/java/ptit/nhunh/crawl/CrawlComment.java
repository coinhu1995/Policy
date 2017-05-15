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

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.dao.DBDAO;
import ptit.nhunh.dao.UrlDAO;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Item;
import ptit.nhunh.model.ResponseObject;
import ptit.nhunh.model.Url;
import ptit.nhunh.tool.ConvertJson2Java;;

public class CrawlComment {
	private static String urlComment = "http://usi.saas.vnexpress.net/index/"
			+ "get?offset=0&limit=1000&objecttype=1&siteid=1000000&objectid=";
	private static String urlReply = "http://usi.saas.vnexpress.net/index/getreplay?"
			+ "siteid=1000000&objecttype=1&limit=500&offset=0&";
	private CommentDAO cmtDAO = new CommentDAO("Capstone");
	private UrlDAO urlDAO = new UrlDAO("Capstone");
	private BufferedWriter bw;

	public static void main(String[] args) throws SQLException, IOException {
		new CrawlComment().process();
//		new CrawlComment().crawlParentComment("3325882");
	}

	public void process() throws SQLException, IOException {
		this.bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File("CrawlCommentLogFile.txt"))));
		System.out.println("Crawling...");

//		this.dbConnect.updateData("delete from TblCmt");
//		this.dbConnect.updateData("DBCC CHECKIDENT ('TblCmt', RESEED, 0)");

		ArrayList<Url> listURL = this.urlDAO.getAllNeededUrl();

		for (Url u : listURL) {
			this.crawlParentComment(u.getUrl_id());
			System.out.println(u.getId() + " done!");
		}

		this.bw.close();
	}

	private void crawlParentComment(String pageId) {
		Document doc;
		try {
			doc = Jsoup.connect(urlComment + pageId).ignoreContentType(true).get();
		} catch (IOException e1) {
			try {
				this.bw.write(e1.getMessage() + " " + pageId);
				this.bw.newLine();
			} catch (IOException e) {
				System.out.println("Loi ghi file: "+e.getMessage()+ " "+pageId );
			}
			e1.printStackTrace();
			return;
		}

		String s = doc.getElementsByTag("body").get(0).text();
		ConvertJson2Java cvt = new ConvertJson2Java();
		ResponseObject data = null;

		try {
			if(pageId.equals("3174878"))
				s = s.replaceAll("\"Nhân dân tệ\"", "Nhân dân tệ");
			data = cvt.convert(s);
		} catch (Exception e) {
			System.out.println(pageId);
			System.out.println(s);
			System.out.println("Loi convert json: "+e.getMessage()+ " "+pageId );
			return;
		}

		for (int i = 0; i < Integer.parseInt(data.getData().getTotal()); i++) {
			Item it = data.getData().getItem()[i];
			String content = it.getContent();
			content = content.replace("'", "");

			String sql = "insert into TblCmt values('" + it.getComment_id() + "',N'" + content
					+ "','" + it.getTime() + "',N'" + it.getArticle_id() + "',N'"
					+ it.getAvatar_original() + "',N'" + it.getLike_ismember() + "',N'"
					+ it.getUserid() + "','" + it.getCreation_time() + "',N'" + it.getAvatar()
					+ "',N'" + it.getType() + "',N'" + it.getUserlike() + "',N'"
					+ it.getFull_name().replaceAll("'", "\"") + "','" + it.getParent_id()
					+ "','', '')";
			try {
				this.cmtDAO.updateData(sql);
			} catch (SQLException e) {
				try {
					System.out.println(pageId);
					System.out.println(sql);
					this.bw.write("crawl parent comment: " + e.getMessage() + " " + pageId);
					this.bw.newLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}

	private void crawlReplyComment(String pageId, String commentId) throws IOException {
		String urlRep = urlReply + "objectid=" + pageId + "&id=" + commentId;
		Document doc = Jsoup.connect(urlRep).ignoreContentType(true).timeout(1000000).get();
		String s = doc.getElementsByTag("body").get(0).text();
		ConvertJson2Java cvt = new ConvertJson2Java();
		ResponseObject data = cvt.convert(s);
		for (int i = 0; i < data.getData().getItem().length; i++) {
			Item it = data.getData().getItem()[i];
			try {
				this.cmtDAO.updateData("insert into TblComment values('" + it.getComment_id()
						+ "',N'" + it.getContent() + "','" + it.getTime() + "',N'"
						+ it.getArticle_id() + "',N'" + it.getAvatar_original() + "',N'"
						+ it.getLike_ismember() + "',N'" + it.getUserid() + "','"
						+ it.getCreation_time() + "',N'" + it.getAvatar() + "',N'" + it.getType()
						+ "',N'" + it.getUserlike() + "',N'" + it.getFull_name() + "','"
						+ it.getParent_id() + "', '', '')");
			} catch (SQLException e) {
				System.out.println("Loi crawl comment parent: pageid = " + pageId + " commentid = "
						+ commentId);
				try {
					this.bw.write("crawl reply comment: " + e.getMessage() + " " + pageId);
					this.bw.newLine();
				} catch (IOException e1) {
					System.out.println(e1.getMessage());
				}
				e.printStackTrace();
			}
		}
	}
}
