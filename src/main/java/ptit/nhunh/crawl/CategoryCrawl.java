package ptit.nhunh.crawl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Url;
import ptit.nhunh.utils.Utils;

public class CategoryCrawl {
	public static void main(String[] args) throws SQLException, IOException {
		SQLDAO urlDao = SQLDAOFactory.getDAO(SQLDAOFactory.URL);
		List<Object> listUrl = urlDao.getAll();

		for (int i = 0; i < listUrl.size(); i++) {
			Document doc = Utils.getHtml(((Url) listUrl.get(i)).getUrl());
			Elements li_s = doc.getElementsByClass("start");
			System.out.println(li_s.get(0));
		}
	}
}
