package ptit.nhunh.classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Classifier {
	public void execute()
			throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
		System.out.println("Processing...");
		Scanner scan = new Scanner(new File("src\\main\\resources\\path.txt"));
		String path = scan.nextLine();
		new Classify().classified(path + "\\input.train", path + "\\input.test", path + "\\output");
		scan.close();
	}
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, SQLException, IOException {
		new Classifier().execute();
	}
}
