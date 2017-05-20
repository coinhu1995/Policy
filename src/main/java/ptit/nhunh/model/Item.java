package ptit.nhunh.model;

public class Item {
	private String content;

	private String comment_id;

	private String time;

	private String article_id;

	private String avatar_original;

	private String like_ismember;

	private String userid;

	private String creation_time;

	private String avatar;

	private String type;

	private String userlike;

	private String full_name;

	private String parent_id;

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getComment_id() {
		return this.comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getArticle_id() {
		return this.article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getAvatar_original() {
		return this.avatar_original;
	}

	public void setAvatar_original(String avatar_original) {
		this.avatar_original = avatar_original;
	}

	public String getLike_ismember() {
		return this.like_ismember;
	}

	public void setLike_ismember(String like_ismember) {
		this.like_ismember = like_ismember;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCreation_time() {
		return this.creation_time;
	}

	public void setCreation_time(String creation_time) {
		this.creation_time = creation_time;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserlike() {
		return this.userlike;
	}

	public void setUserlike(String userlike) {
		this.userlike = userlike;
	}

	public String getFull_name() {
		return this.full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getParent_id() {
		return this.parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	@Override
	public String toString() {
		return "ClassPojo [content = " + this.content + ", comment_id = " + this.comment_id + ", time = " + this.time
				+ ", article_id = " + this.article_id + ", avatar_original = " + this.avatar_original + ", like_ismember = "
				+ this.like_ismember + ", userid = " + this.userid + ", creation_time = " + this.creation_time + ", avatar = " + this.avatar
				+ ", type = " + this.type + ", userlike = " + this.userlike + ", full_name = " + this.full_name + ", parent_id = "
				+ this.parent_id + "]";
	}
	
	public Comment convert2Comment(){
		Comment c = new Comment();
		c.setAvatar(this.avatar);
		c.setAvatar_original(this.avatar_original);
		c.setCmt_id(this.comment_id);
		c.setCmt_segment("");
		c.setContent(this.content);
		c.setCreate_time(this.creation_time);
		c.setFullname(this.full_name);
		c.setId(-1);
		c.setLabel(0);
		c.setLabel2(0);
		c.setLike_ismember(this.like_ismember);
		c.setPage_id(this.article_id);
		c.setParent_id(this.parent_id);
		c.setTime(this.time);
		c.setType(Integer.parseInt(this.type));
		c.setUser_id(this.userid);
		c.setUserlike(Integer.parseInt(this.userlike));
		
		return c;
	}
}