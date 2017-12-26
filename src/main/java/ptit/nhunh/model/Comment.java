package ptit.nhunh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String cmt_id;
	@Getter
	@Setter
	private String content;
	@Getter
	@Setter
	private String cmt_segment;
	@Getter
	@Setter
	private String time;
	@Getter
	@Setter
	private String page_id;
	@Getter
	@Setter
	private String avatar_original;
	@Getter
	@Setter
	private String like_ismember;
	@Getter
	@Setter
	private String user_id;
	@Getter
	@Setter
	private String create_time;
	@Getter
	@Setter
	private String avatar;
	@Getter
	@Setter
	private int type;
	@Getter
	@Setter
	private int userlike;
	@Getter
	@Setter
	private String fullname;
	@Getter
	@Setter
	private String parent_id;
	@Getter
	@Setter
	private int label;
	@Getter
	@Setter
	private int label2;
	@Getter
	@Setter
	private int articleid;

	@Override
	public String toString() {
		return "Comment [id=" + this.id + ", cmt_id=" + this.cmt_id + ", content=" + this.content + ", cmt_segment="
				+ this.cmt_segment + ", time=" + this.time + ", page_id=" + this.page_id + ", avatar_original="
				+ this.avatar_original + ", like_ismember=" + this.like_ismember + ", user_id=" + this.user_id
				+ ", create_time=" + this.create_time + ", avatar=" + this.avatar + ", type=" + this.type
				+ ", userlike=" + this.userlike + ", fullname=" + this.fullname + ", parent_id=" + this.parent_id
				+ ", label=" + this.label + ", label2=" + this.label2 + "]";
	}
}
