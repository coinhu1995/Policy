package ptit.nhunh.context;

import lombok.Getter;
import lombok.Setter;

public class Context {

	private static Context instance = new Context();

	@Getter
	@Setter
	private String commentTableName = "TblCommentTest";

	@Getter
	@Setter
	private int train = 200;

	@Getter
	@Setter
	private int test = 300;

	private Context() {

	}

	public static Context getInstance() {
		return Context.instance;
	}
}
