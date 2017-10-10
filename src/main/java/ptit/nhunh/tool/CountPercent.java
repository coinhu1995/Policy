package ptit.nhunh.tool;

import java.sql.SQLException;
import java.util.List;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.model.Comment;
import ptit.nhunh.utils.Utils;

public class CountPercent {
	private SQLDAO articleDao = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
	private SQLDAO cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
	
	public static void main(String[] args) throws SQLException {
		new CountPercent().execute();
	}
	
	private void execute() throws SQLException {
		List<Article> listArticle = Utils.object2Article(this.articleDao.getAll());
		List<Comment> listCmt = Utils.object2Comment(this.cmtDao.getAll());

		for (int i = 0; i < listArticle.size(); i++) {
			int positiveLabel = 0, negativeLabel = 0;
			for (int j = 0; j < listCmt.size(); j++) {
				Comment c = listCmt.get(j);
				if (c.getPage_id().equals(listArticle.get(i).getUrl_id())) {
					if (c.getLabel2() == 1) {
						positiveLabel++;
					} else {
						negativeLabel++;
					}
				}
			}
			int dongY = positiveLabel / (positiveLabel + negativeLabel) * 100;
			listArticle.get(i).setDongy(dongY);
			listArticle.get(i).setYkienkhac(100 - dongY);
			this.articleDao.update(listArticle.get(i));
		}
	}
}
