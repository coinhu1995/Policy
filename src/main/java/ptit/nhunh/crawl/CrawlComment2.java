package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ptit.nhunh.dao.CommentDAO;
import ptit.nhunh.model.Item;
import ptit.nhunh.model.ResponseObject;
import ptit.nhunh.tool.ConvertJson2Java;

/**
 * Crawl khi url chuẩn.
 * 
 * @author uhn
 *
 */
public class CrawlComment2 {
	private static String urlComment = "http://usi.saas.vnexpress.net/index/"
			+ "get?offset=0&limit=300&objecttype=1&siteid=1000000&objectid=";
	private static String urlReply = "http://usi.saas.vnexpress.net/index/getreplay?"
			+ "siteid=1000000&objecttype=1&limit=50&offset=0&";
	private CommentDAO cmtDAO = new CommentDAO("Capstone");

	public static void main(String[] args) throws SQLException {
		System.out.println("Crawling...");
		CommentDAO cmtDAO = new CommentDAO("Capstone");
		cmtDAO.updateData("delete from TblComment");
		cmtDAO.updateData("DBCC CHECKIDENT ('TblComment', RESEED, 0)");
		ResultSet rs = cmtDAO.getData("select * from TblUrl order by id");
		while (rs.next()) {
			String url = rs.getString(2);
			String pageId = getPageId(url);
			cmtDAO.updateData("update TblUrl set needed = 1 where url = '" + url + "'");
			new CrawlComment2().crawlComment(pageId, rs.getInt(1));
			System.out.println(rs.getInt(1) + ": OKKKKKKKK");
		}
		System.out.println("done!");
	}

	private void crawlComment(String pageId, int id) {
		Document doc = null;
		try {
			doc = Jsoup.connect(urlComment + pageId).ignoreContentType(true).get();
		} catch (IOException e1) {
			System.out.println("Loi " + e1.getMessage() + " o Comment");
			return;
		}
		String s = doc.getElementsByTag("body").get(0).text();
		ConvertJson2Java cvt = new ConvertJson2Java();
		ResponseObject data = null;
		if (s.indexOf("Thành \"Nhân dân tệ\"") > 0) {
			s = s.replace("Thành \"Nhân dân tệ\"", "Vô thường");
		}
		data = cvt.convert(s);
		for (int i = 0; i < Integer.parseInt(data.getData().getTotal()); i++) {
			Item it = data.getData().getItem()[i];
			String content = it.getContent();
			while (content.indexOf("'") >= 0) {
				content = content.replace("'", "");
			}
			content = content.replace("'", "");

			String sql = "insert into TblComment values('" + it.getComment_id() + "',N'" + content
					+ "','" + it.getTime() + "',N'" + it.getArticle_id() + "',N'"
					+ it.getAvatar_original() + "',N'" + it.getLike_ismember() + "',N'"
					+ it.getUserid() + "','" + it.getCreation_time() + "',N'" + it.getAvatar()
					+ "',N'" + it.getType() + "',N'" + it.getUserlike() + "',N'"
					+ it.getFull_name().replace("'", "") + "','" + it.getParent_id() + "','')";
			try {
				this.cmtDAO.updateData(sql);
				crawlReplyComment(pageId, it.getComment_id());
			} catch (Exception e) {
				System.out.println("Loi crawl comment parent, pageid = " + pageId);
				System.out.println(sql);
				e.printStackTrace();
			}

		}
	}

	private void crawlReplyComment(String pageId, String commentId) {
		String urlRep = urlReply + "objectid=" + pageId + "&id=" + commentId;
		Document doc = null;
		try {
			doc = Jsoup.connect(urlRep).ignoreContentType(true).timeout(10000).get();
		} catch (IOException e1) {
			System.out.println("Loi " + e1.getMessage() + " o Reply");
			return;
		}
		String s = doc.getElementsByTag("body").get(0).text();
		ConvertJson2Java cvt = new ConvertJson2Java();
		ResponseObject data = null;
		data = cvt.convert(s);
		for (int i = 0; i < data.getData().getItem().length; i++) {
			Item it = data.getData().getItem()[i];
			String content = it.getContent();
			while (content.indexOf("'") >= 0) {
				content = content.replace("'", "");
			}
			String sql = "insert into TblComment values('" + it.getComment_id() + "',N'" + content
					+ "','" + it.getTime() + "',N'" + it.getArticle_id() + "',N'"
					+ it.getAvatar_original() + "',N'" + it.getLike_ismember() + "',N'"
					+ it.getUserid() + "','" + it.getCreation_time() + "',N'" + it.getAvatar()
					+ "',N'" + it.getType() + "',N'" + it.getUserlike() + "',N'"
					+ it.getFull_name().replace("'", "") + "','" + it.getParent_id() + "', '')";
			try {
				this.cmtDAO.updateData(sql);
			} catch (SQLException e) {
				System.out.println("Loi crawl reply," + e.getMessage() + " pageid = " + pageId
						+ " commentid = " + commentId);
				System.out.println(sql);
				e.printStackTrace();
			}
		}
	}

	private static String getPageId(String url) {
		return url.substring(url.lastIndexOf("-") + 1, url.lastIndexOf("-") + 8);
	}
}
