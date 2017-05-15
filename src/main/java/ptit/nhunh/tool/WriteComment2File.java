package ptit.nhunh.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ptit.nhunh.model.ResponseObject;

public class WriteComment2File {
	private static String url = "http://usi.saas.vnexpress.net/index/getreplay?siteid=1000000&objectid=3545309&objecttype=1&id=20375934&limit=500&offset=2";

	public void writeComment(String object_id) throws IOException {
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(object_id + ".txt"))));
		Document doc = Jsoup.connect(url + object_id).ignoreContentType(true).get();
		String s = doc.getElementsByTag("body").get(0).text();
		ConvertJson2Java cvt = new ConvertJson2Java();
		ResponseObject data = cvt.convert(s);
		for (int i = 0; i < Integer.parseInt(data.getData().getTotal()); i++) {
			bw.write(data.getData().getItem()[i].getContent().trim());
			bw.newLine();
		}
		bw.close();
	}
}
