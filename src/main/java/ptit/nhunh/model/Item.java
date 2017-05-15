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
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getAvatar_original() {
		return avatar_original;
	}

	public void setAvatar_original(String avatar_original) {
		this.avatar_original = avatar_original;
	}

	public String getLike_ismember() {
		return like_ismember;
	}

	public void setLike_ismember(String like_ismember) {
		this.like_ismember = like_ismember;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(String creation_time) {
		this.creation_time = creation_time;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserlike() {
		return userlike;
	}

	public void setUserlike(String userlike) {
		this.userlike = userlike;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	@Override
	public String toString() {
		return "ClassPojo [content = " + content + ", comment_id = " + comment_id + ", time = " + time
				+ ", article_id = " + article_id + ", avatar_original = " + avatar_original + ", like_ismember = "
				+ like_ismember + ", userid = " + userid + ", creation_time = " + creation_time + ", avatar = " + avatar
				+ ", type = " + type + ", userlike = " + userlike + ", full_name = " + full_name + ", parent_id = "
				+ parent_id + "]";
	}
}