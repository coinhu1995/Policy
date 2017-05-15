package ptit.nhunh.crawl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ptit.nhunh.utils.Utils;

public class CrawlUrl {
	BufferedWriter bw;
	ArrayList<String> listURL = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		CrawlUrl crawl = new CrawlUrl();
		crawl.danTriProcess();
	}

	public void danTriProcess() throws IOException {
		bw = new BufferedWriter(new FileWriter("urlthanhnien.txt"));
		this.listURL.add("http://thanhnien.vn/");
		danTri(0);
	}

	public void danTri(int cnt) throws IOException {
		Document doc = Utils.getHtml(this.listURL.get(cnt));

		if (doc != null) {
			Elements listA = doc.getElementsByTag("a");

			for (int i = 0; i < listA.size(); i++) {
				String url = listA.get(i).attr("href");
				if (url.indexOf(".html") > 0) {
					if (!this.listURL.contains("thanhnien.vn" + url)) {
						this.listURL.add("thanhnien.vn" + url);
						bw.write("thanhnien.vn" + url);
						bw.newLine();
						System.out.println("thanhnien.vn" + url);
					}
				}
			}

			danTri(cnt + 1);
		}
	}
}
