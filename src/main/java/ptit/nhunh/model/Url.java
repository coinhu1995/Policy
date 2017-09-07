package ptit.nhunh.model;

import lombok.Getter;
import lombok.Setter;
import ptit.nhunh.utils.Constants;

public class Url {
	private int id;
	private String url;
	private String url_id;
	private String titles;
	private int needed;
	private String source;
	private int totalParComment;
	private int totalComment;
	private String tag;
	@Getter
	@Setter
	private String category;
	public Url(int id, String url, String url_id, String titles, int needed, String source,
			int totalParComment, int totalComment, String tag, String category) {
		super();
		this.id = id;
		this.url = url;
		this.url_id = url_id;
		this.titles = titles;
		this.needed = needed;
		this.source = source;
		this.totalParComment = totalParComment;
		this.totalComment = totalComment;
		this.tag = tag;
		this.category = category;
	}

	public Url() {
		this.url = Constants.SPACE;
		this.url_id = Constants.SPACE;
		this.titles = Constants.SPACE;
		this.source = Constants.SPACE;
		this.tag = Constants.SPACE;
		this.category = Constants.SPACE;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl_id() {
		return this.url_id;
	}

	public void setUrl_id(String url_id) {
		this.url_id = url_id;
	}

	public String getTitles() {
		return this.titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public int getNeeded() {
		return this.needed;
	}

	public void setNeeded(int needed) {
		this.needed = needed;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getTotalParComment() {
		return this.totalParComment;
	}

	public void setTotalParComment(int totalParComment) {
		this.totalParComment = totalParComment;
	}

	public int getTotalComment() {
		return this.totalComment;
	}

	public void setTotalComment(int totalComment) {
		this.totalComment = totalComment;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
