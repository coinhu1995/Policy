package ptit.nhunh.model;

import java.io.Serializable;

public class Word implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String word;
	private int TF;
	private int DF;
	private float IDF;
	private double TFIDF;
	private int isStop;
	private int cmt_id;

	public Word(int id, String word, int tF, int dF, float iDF, double tFIDF, int isStop,
			int cmt_id) {
		super();
		this.id = id;
		this.word = word;
		this.TF = tF;
		this.DF = dF;
		this.IDF = iDF;
		this.TFIDF = tFIDF;
		this.isStop = isStop;
		this.cmt_id = cmt_id;
	}

	public Word() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getTF() {
		return this.TF;
	}

	public void setTF(int tF) {
		this.TF = tF;
	}

	public int getDF() {
		return this.DF;
	}

	public void setDF(int dF) {
		this.DF = dF;
	}

	public float getIDF() {
		return this.IDF;
	}

	public void setIDF(float iDF) {
		this.IDF = iDF;
	}

	public double getTFIDF() {
		return this.TFIDF;
	}

	public void setTFIDF(double tFIDF) {
		this.TFIDF = tFIDF;
	}

	public int getIsStop() {
		return this.isStop;
	}

	public void setIsStop(int isStop) {
		this.isStop = isStop;
	}

	public int getCmt_id() {
		return this.cmt_id;
	}

	public void setCmt_id(int cmt_id) {
		this.cmt_id = cmt_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
