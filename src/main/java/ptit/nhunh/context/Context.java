package ptit.nhunh.context;

public class Context {
	public static String COMMENTTABLENAME = "TblCommentTest";
	public static int TRAINSIZE = 200;
	public static int TESTSIZE = 100;

	/**
	 * 1: binary : 0, 1 <br>
	 * 2: times_occur
	 * 3: 1 + log (times_occur)
	 * 4: frequency
	 */
	public static int TYPEOFTF = 2;

	/**
	 * 1: inverse document frequency : log(N/DF) <br>
	 * 2: inverse document frequency smooth : log(1 + N/DF)
	 */
	public static int TYPEOFIDF = 2;
	
	/**
	 *    TRAIN/TEST
	 * 1: 200/100
	 * 2: 100/100
	 */
	public static int TYPEOFCOPYDATA2DATABASE = 1;
	
	/**
	 * 1: Normal
	 * 2: ARFF
	 */
	public static int TYPEOFFILE = 1;
}
