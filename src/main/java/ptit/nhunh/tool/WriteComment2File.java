package ptit.nhunh.tool;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;
import ptit.nhunh.model.Comment;

public class WriteComment2File {
	SQLDAO cmtDAO = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
	SQLDAO articleDAO = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);

	public static void main(String[] args) throws SQLException, IOException {
		new WriteComment2File().writeFile("Comment.txt");
	}

	public void writeFile(String fileName) throws SQLException, IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

		ArrayList<Object> listCmt = this.cmtDAO.getAll();
		for (Object o : listCmt) {
			Comment c = (Comment) o;
			try {
				try {
					Article article = (Article) this.articleDAO.findByItemId(c.getPage_id());
					bw.write(String.format("%-12s", c.getId()) + String.format("%-200s", article.getUrl())
							+ c.getContent());
					bw.newLine();
				} catch (Exception e) {
					System.out.println(c.getId());
					bw.write(String.format("%-12s", c.getId()) + String.format("%-200s", "khong co")
							+ c.getContent());
					bw.newLine();
				}

			} catch (Exception e) {
				System.out.println(c.getId());
				throw e;
			}
		}
		bw.close();
	}
}
