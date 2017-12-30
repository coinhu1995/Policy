package ptit.nhunh.model;

import java.io.Serializable;
import java.sql.Date;

import javax.faces.bean.ManagedBean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@ManagedBean
public class Article implements Serializable{
	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String url;
	@Getter
	@Setter
	private String url_id;
	@Getter
	@Setter
	private String title;
	@Getter
	@Setter
	private int needed;
	@Getter
	@Setter
	private String source;
	@Getter
	@Setter
	private int totalComment;
	@Getter
	@Setter
	private int totalParComment;
	@Getter
	@Setter
	private String tag;
	@Getter
	@Setter
	private String category;
	@Getter
	@Setter
	private Date creationTime;
	@Getter
	@Setter
	private String contentFilePath;
	@Getter
	@Setter
	private String imageUrl;
	@Getter
	@Setter
	private int dongy;
	@Getter
	@Setter
	private int khongdongy;
	@Getter
	@Setter
	private int isCrawled;
}
