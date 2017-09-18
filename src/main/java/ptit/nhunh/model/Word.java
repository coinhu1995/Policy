package ptit.nhunh.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import ptit.nhunh.context.Context;

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
	 * 1: stop word
	 * 0: not stop word
	 */
	@Getter
	@Setter
	private int isStopWord;
	
	@Getter
	@Setter
	private int cmt_id;

	public float getTF(int sumWord) {
		if (Context.TYPEOFTF == 1) {
			return 1;
		} else if (Context.TYPEOFTF == 2) {
			return this.timesOccur;
		} else if (Context.TYPEOFTF == 3) {
			return (float) (1 + Math.log10(this.timesOccur));
		} else if (Context.TYPEOFTF == 4) {
			return this.timesOccur / (float)sumWord;
		} else {
			return 0;
		}
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
	public float getTFIDF(int N, int sumWord) {
		return this.getTF(sumWord) * this.getIDF(N);
	}

	public Word(int id, String word, float frequency, int timesOccur, int dF, int isStop,
			int cmt_id) {
		super();
		this.id = id;
		this.word = word;
		this.frequency = frequency;
		this.timesOccur = timesOccur;
		this.DF = dF;
		this.isStopWord = isStop;
		this.cmt_id = cmt_id;
	}

	public Word() {
		super();
	}
}
