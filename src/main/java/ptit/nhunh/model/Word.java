package ptit.nhunh.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.nhunh.context.Context;

@NoArgsConstructor
@AllArgsConstructor
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
	private float frequency;

	@Getter
	@Setter
	private int timesOccur;

	@Getter
	@Setter
	private int DF;

	/**
	 * 1: stop word 0: not stop word
	 */
	@Getter
	@Setter
	private boolean isStopWord;

	@Getter
	@Setter
	private int cmt_id;

	public float getTF() {
		if (Context.TYPEOFTF == 1) {
			return 1;
		} else if (Context.TYPEOFTF == 2) {
			return this.timesOccur;
		} else if (Context.TYPEOFTF == 3) {
			return (float) (1 + Math.log10(this.timesOccur));
		} else if (Context.TYPEOFTF == 4) {
			// TODO
		}
		return 0;
	}

	/**
	 * get IDF.
	 * 
	 * @param totalComment
	 *            so luong ban ghi trong tap.
	 * @return
	 */
	public float getIDF(int totalComment) {
		if (Context.TYPEOFIDF == 1) {
			return (float) Math.log10(totalComment / (float) this.DF);
		} else if (Context.TYPEOFIDF == 2) {
			return (float) Math.log10(1 + totalComment / (float) this.DF);
		} else {
			return 1;
		}
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
