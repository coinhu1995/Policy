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
					if (c.getLabel() == 1) {
						positiveLabel++;
					} else {
						negativeLabel++;
					}
				}
			}
			int dongY = 0;
			try {
				dongY = (int) (positiveLabel / (float) (positiveLabel + negativeLabel) * 100);
				listArticle.get(i).setDongy(dongY);
				listArticle.get(i).setKhongdongy(100 - dongY);
			} catch (Exception e) {
				listArticle.get(i).setDongy(0);
				listArticle.get(i).setKhongdongy(0);
				System.out.println(listArticle.get(i).getId() + " " + e.getMessage());
			}
			
			this.articleDao.update(listArticle.get(i));
			System.out.println(i + " done");
		}
	}
}
