package ptit.nhunh.model;

import java.io.Serializable;

public class Word implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String word;
	private int TF;
	private float DF;
	private float IDF;
	private double TFIDF;
	private int isStop;
	
	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getTF() {
		return this.TF;
	}

	public void setTF(int quantity) {
		this.TF = quantity;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Word() {
		super();
	}

	public Word(int id, String word, int TF, float dF, float iDF, double tFIDF, int isStop) {
		super();
		this.id = id;
		this.word = word;
		this.TF = TF;
		this.DF = dF;
		this.IDF = iDF;
		this.TFIDF = tFIDF;
		this.isStop = isStop;
	}

	public float getDF() {
		return this.DF;
	}

	public void setDF(float dF) {
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

}
