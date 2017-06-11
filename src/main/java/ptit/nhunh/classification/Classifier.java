package ptit.nhunh.classification;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import ptit.nhunh.Classify;

public class Classifier {
	public static void main(String[] args) throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
		System.out.println("Processing...");

		String path = "20170609\\2049";

		new Classify().classified("src\\main\\resource\\data\\2label\\" + path + "\\input.train", "src\\main\\resource\\data\\2label\\" + path + "\\input.test",
				"src\\main\\resource\\data\\2label\\" + path + "\\output");
	}
}
