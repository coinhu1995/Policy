package ptit.nhunh.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

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
	private int frequency;
	@Getter
	@Setter
	private double TF;
	@Getter
	@Setter
	private int DF;
	@Getter
	@Setter
	private double IDF;
	@Getter
	@Setter
	private double TFIDF;
	@Getter
	@Setter
	private int isStop;
	@Getter
	@Setter
	private int cmt_id;

	public Word(int id, String word, int frequency, double tF, int dF, double iDF, double tFIDF,
			int isStop, int cmt_id) {
		super();
		this.id = id;
		this.word = word;
		this.frequency = frequency;
		this.TF = tF;
		this.DF = dF;
		this.IDF = iDF;
		this.TFIDF = tFIDF;
		this.isStop = isStop;
		this.cmt_id = cmt_id;
	}

	public void print() {
		System.out.println(String.format("%25s", this.word) + String.format("%10s", this.DF)
				+ String.format("%20s", this.TF) + String.format("%20s", this.IDF));
	}

}
