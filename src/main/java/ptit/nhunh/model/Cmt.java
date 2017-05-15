package ptit.nhunh.model;

public class Cmt {
	private int id;
	private String content;
	private String url;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Cmt(int id, String content, String url) {
		super();
		this.id = id;
		this.content = content;
		this.url = url;
	}
	public Cmt() {
	}
}
