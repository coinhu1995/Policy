package ptit.nhunh.prepare2;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Prepare data với chỉ 2 nhãn 1, 3 của label2 phân từ sử dụng vntokenizer
 * 
 * @author uhn
 *
 */
public class PrepareData {

	public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		int train = 4000;
		int test = 5809;

		System.out.println("--- Start Processing ---");
		long sSentenceSegment = System.currentTimeMillis();
		System.out.println("\t+> Sentence Segmenting...");
//		new SentenceSegment().process();
		long eSentenceSegment = System.currentTimeMillis();
		System.out.println("\t+> Word Collecting...");
		new CollectWord().process(train);
		long eCollectWord = System.currentTimeMillis();
		System.out.println("\t+> File Generating...");
		new GenTrainingFile().process(train, test);
		long eGenDataFile = System.currentTimeMillis();
		System.out.println("--- End Processing ---");

		System.out.println(
				"Sentence Segment: " + (eSentenceSegment - sSentenceSegment) / (float) 60000);
		System.out
				.println("Collect Word    : " + (eCollectWord - eSentenceSegment) / (float) 60000);
		System.out.println("Generate File   : " + (eGenDataFile - eCollectWord) / (float) 60000);
	}
}
