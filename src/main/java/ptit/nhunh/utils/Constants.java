package ptit.nhunh.utils;

public class Constants {
	// Đồng ý với bài viết.
	public static final String DONG_Y = new String("1");
	// Không đồng ý với bài viết
	public static final String KHONG_DONG_Y = new String("2");
	// Thắc mắc
	public static final String THAC_MAC = new String("3");
	// SPACE
	public static final String SPACE = new String(" ");
	/**
	 * Url to crawl comment thanhnien.vn VD:
	 * http://thanhnien.vn/ajax/comment-657775-3-like.html
	 */
	public static final String TNURL = new String("http://thanhnien.vn/ajax/comment-");

	public static final String LOG_PATH = "src\\main\\resources\\log\\";
	public static final String DATA_PATH = "src\\main\\resources\\data\\";
	public static final String CSS = "src\\main\\resources\\css\\vnexpress\\";
	public static final String VNEXPRESS_ARTICLE_PATH = "src\\main\\resources\\article\\vnexpress\\";
	public static final String THANHNIEN_ARTICLE_PATH = "src\\main\\resources\\article\\thanhnien\\";
	public static final String PREFIX_CONTENT = "<!DOCTYPE html [ <!ENTITY nbsp \"&#160;\"> ]><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:h=\"http://java.sun.com/jsf/html\" xmlns:ui=\"http://java.sun.com/jsf/facelets\"><h:head><style>.sidebar_1 {width: 100% !important;}</style></h:head><h:body>";
	public static final String SUFFIX_CONTENT = "</h:body></html>";
}
