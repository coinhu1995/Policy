package ptit.nhunh.model;

import ptit.nhunh.utils.Constants;

public class Url {
	private int id;
	private String url;
	private String url_id;
	private String tittle;
	private int needed;
	private String source;
	private int totalParComment;
	private int totalComment;
	private String tag;
	public Url(int id, String url, String url_id, String tittle, int needed, String source,
			int totalParComment, int totalComment, String tag) {
		super();
		this.id = id;
		this.url = url;
		this.url_id = url_id;
		this.tittle = tittle;
		this.needed = needed;
		this.source = source;
		this.totalParComment = totalParComment;
		this.totalComment = totalComment;
		this.tag = tag;
	}
	public Url() {
		this.url = Constants.SPACE;
		this.url_id = Constants.SPACE;
		this.tittle = Constants.SPACE;
		this.source = Constants.SPACE;
		this.tag = Constants.SPACE;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl_id() {
		return url_id;
	}
	public void setUrl_id(String url_id) {
		this.url_id = url_id;
	}
	public String getTittle() {
		return tittle;
	}
	public void setTittle(String tittle) {
		this.tittle = tittle;
	}
	public int getNeeded() {
		return needed;
	}
	public void setNeeded(int needed) {
		this.needed = needed;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getTotalParComment() {
		return totalParComment;
	}
	public void setTotalParComment(int totalParComment) {
		this.totalParComment = totalParComment;
	}
	public int getTotalComment() {
		return totalComment;
	}
	public void setTotalComment(int totalComment) {
		this.totalComment = totalComment;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
