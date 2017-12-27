package ptit.nhunh.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Word implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private int id;

	@Getter
	@Setter
	private String word;

	@Getter
	@Setter
	private int timesOccur;

	@Getter
	@Setter
	private int DF;

	@Getter
	@Setter
	private boolean isStopWord;

	public float getTF() {
		return this.timesOccur;
	}

	/**
	 * get IDF.
	 * 
	 * @param totalComment
	 *            so luong ban ghi trong tap.
	 * @return
	 */
	public float getIDF(int totalComment) {
		return (float) Math.log10(1 + totalComment / (float) this.DF);
	}

	/**
	 * get TFIDF.
	 * 
	 * @param N
	 *            so luong ban ghi trong tap.
	 * @return
	 */
	public float getTFIDF(int N) {
		return this.getTF() * this.getIDF(N);
	}

}
