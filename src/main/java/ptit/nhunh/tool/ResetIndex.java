package ptit.nhunh.tool;

import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.model.Comment;
import ptit.nhunh.model.Word;

public class ResetIndex {
	private SQLDAO urlDAO;
	private SQLDAO commentDAO;
	private SQLDAO wordDAO;

	public ResetIndex() {
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
		this.commentDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.wordDAO = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);
	}

	public static void main(String[] args) throws SQLException {
		new ResetIndex().resetIndexTblComment();
		new ResetIndex().resetIndexTblUrl();
		new ResetIndex().resetIndexTblWord();
	}

	public void resetIndexTblComment() throws SQLException {
		ArrayList<Object> listCmt = this.commentDAO.getAll();

		this.commentDAO.update("delete from TblComment");
		this.commentDAO.update("DBCC CHECKIDENT ('TblComment', RESEED, 0)");

		for (Object o : listCmt) {
			Comment c = (Comment) o;
			this.commentDAO.insert(c);
		}
	}

	public void resetIndexTblUrl() throws SQLException {
		ArrayList<Object> listCmt = this.urlDAO.getAll();

		this.urlDAO.update("delete from TblArticle");
		this.urlDAO.update("DBCC CHECKIDENT ('TblArticle', RESEED, 0)");

		for (Object o : listCmt) {
			Article url = (Article) o;
			this.urlDAO.insert(url);
		}
	}

	public void resetIndexTblWord() throws SQLException {
		ArrayList<Object> listCmt = this.wordDAO.getAll();

		this.wordDAO.update("delete from TblWord");
		this.wordDAO.update("DBCC CHECKIDENT ('TblWord', RESEED, 0)");

		for (Object o : listCmt) {
			Word word = (Word) o;
			this.wordDAO.insert(word);
		}
	}
}
