package ptit.nhunh.model;

import ptit.nhunh.utils.Constants;

public class Comment {
	private int id; 
	private String cmt_id;
	private String content;
	private String cmt_segment;
	private String time;
	private String page_id;
	private String avatar_original;
	private String like_ismember;
	private String user_id;
	private String create_time;
	private String avatar;
	private int type;
	private int userlike;
	private String fullname;
	private String parent_id;
	private int label;
	private int label2;
	
	public Comment(int id, String cmt_id, String content, String cmt_segment, String time,
			String page_id, String avatar_original, String like_ismember, String user_id,
			String create_time, String avatar, int type, int userlike, String fullname,
			String parent_id, int label, int label2) {
		super();
		this.id = id;
		this.cmt_id = cmt_id;
		this.content = content;
		this.cmt_segment = cmt_segment;
		this.time = time;
		this.page_id = page_id;
		this.avatar_original = avatar_original;
		this.like_ismember = like_ismember;
		this.user_id = user_id;
		this.create_time = create_time;
		this.avatar = avatar;
		this.type = type;
		this.userlike = userlike;
		this.fullname = fullname;
		this.parent_id = parent_id;
		this.label = label;
		this.label2 = label2;
	}
	public Comment() {
		this.cmt_id = Constants.SPACE;
		this.content = Constants.SPACE;
		this.cmt_segment = Constants.SPACE;
		this.time = Constants.SPACE;
		this.page_id = Constants.SPACE;
		this.avatar_original = Constants.SPACE;
		this.like_ismember = Constants.SPACE;
		this.user_id = Constants.SPACE;
		this.create_time = Constants.SPACE;
		this.avatar = Constants.SPACE;
		this.fullname = Constants.SPACE;
		this.parent_id = Constants.SPACE;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCmt_id() {
		return cmt_id;
	}
	public void setCmt_id(String cmt_id) {
		this.cmt_id = cmt_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCmt_segment() {
		return cmt_segment;
	}
	public void setCmt_segment(String cmt_segment) {
		this.cmt_segment = cmt_segment;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPage_id() {
		return page_id;
	}
	public void setPage_id(String page_id) {
		this.page_id = page_id;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUserlike() {
		return userlike;
	}
	public void setUserlike(int userlike) {
		this.userlike = userlike;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public int getLabel2() {
		return label2;
	}
	public void setLabel2(int label2) {
		this.label2 = label2;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", cmt_id=" + cmt_id + ", content=" + content
				+ ", cmt_segment=" + cmt_segment + ", time=" + time + ", page_id=" + page_id
				+ ", avatar_original=" + avatar_original + ", like_ismember=" + like_ismember
				+ ", user_id=" + user_id + ", create_time=" + create_time + ", avatar=" + avatar
				+ ", type=" + type + ", userlike=" + userlike + ", fullname=" + fullname
				+ ", parent_id=" + parent_id + ", label=" + label + ", label2=" + label2 + "]";
	}
}
