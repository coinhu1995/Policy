package ptit.nhunh.dao;

public class SQLDAOFactory {
	public static final int COMMENT = 1;
	public static final int ARTICLE = 2;
	public static final int WORD = 3;
	public static final int COMMENTTEST = 4;
	
	public static SQLDAO getDAO(int type){
		switch(type){
		case COMMENT:
			return CommentDAO.getInstance();
		case ARTICLE:
			return ArticleDAO.getInstance();
		case WORD:
			return WordDAO.getInstance();
		case COMMENTTEST:
			return CommentTestDAO.getInstance();
		}
		return null;
	}
}
