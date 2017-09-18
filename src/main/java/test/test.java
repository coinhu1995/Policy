package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Article;

public class test {
	public static void main(String[] args) throws IOException, SQLException, ParseException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File("temp.txt")), StandardCharsets.UTF_8));
		SQLDAO articleDao = SQLDAOFactory.getDAO(SQLDAOFactory.ARTICLE);

		int dem = 1;
		Article article = new Article();
		while (dem < 13053) {
			System.out.println(dem);
			article.setId(Integer.parseInt(br.readLine()));
			article.setUrl(br.readLine());
			article.setUrl_id(br.readLine());
			article.setTitle(br.readLine());
			article.setNeeded(Integer.parseInt(br.readLine()));
			article.setSource(br.readLine());
			article.setTotalComment(Integer.parseInt(br.readLine()));
			article.setTotalParComment(Integer.parseInt(br.readLine()));
			article.setTag(br.readLine());
			String s = "";
			if ((s = br.readLine()) != null) {
				article.setCategory(s);
			} else {
				article.setCategory(" ");
			}
			br.readLine();
			article.setCreationTime(new Date(12345));
			if ((s = br.readLine()) != null) {
				article.setContentFilePath(s);
			} else {
				article.setContentFilePath("");
			}
			if ((s = br.readLine()) != null) {
				article.setImageUrl(s);
			} else {
				article.setImageUrl("");
			}

			try {
				Article a = (Article) articleDao.findByItemId(article.getUrl_id());
			} catch (Exception e) {
				articleDao.insert(article);
			}
		}
		br.close();
	}
}
