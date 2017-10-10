package test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class test {
	public static void main(String[] args) throws IOException, SQLException, ParseException {
		Document doc = Jsoup.parse("<p><a></a></p>");
		Elements es = doc.getAllElements();
		
		System.out.println(es);
	}
}
