package ptit.nhunh.segment;

import vn.hus.nlp.tokenizer.VietTokenizer;

public class WordProcess {
	/**
	 * 
	 * Tách từ có trong file input và ghi kết quả ra file output
	 * 
	 * @param input
	 * @param output
	 */
	public void segment(String input, String output) {
		VietTokenizer vt = new VietTokenizer();
		vt.tokenize(input, output);
	}
}
