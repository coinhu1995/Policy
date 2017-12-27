package ptit.nhunh.tool;

import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.model.Comment;

public class resetIndex {
	private SQLDAO urlDAO;
	private SQLDAO commentDAO;
	private SQLDAO wordDAO;

	public resetIndex() {
		this.urlDAO = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);
		this.commentDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
		this.wordDAO = SQLDAOFactory.getDAO(SQLDAOFactory.WORD);
	}

	public static void main(String[] args) throws SQLException {
		new resetIndex().resetIndexTblComment();
		new resetIndex().resetIndexTblUrl();
		new resetIndex().resetIndexTblWord();
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

		this.urlDAO.update("delete from TblUrl");
		this.urlDAO.update("DBCC CHECKIDENT ('TblUrl', RESEED, 0)");

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
			Article url = (Article) o;
			this.wordDAO.insert(url);
		}
	}
}
