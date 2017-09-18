package ptit.nhunh.naivebayes;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import ptit.nhunh.model.Comment;

public class RunnableExample {

	public static void main(String[] args) throws SQLException {
		final Classifier<String, String> bayes = new BayesClassifier<String, String>();

		SQLDAO cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENTTEST);
		List<Object> listCmt = cmtDao.getData("select * from TblCommentTest order by id");

		for (int i = 0; i < listCmt.size(); i++) {
			Comment cmt = (Comment) listCmt.get(i);
			System.out.println(cmt.getCmt_segment());
			if (i < 200) {
				bayes.learn(String.valueOf(cmt.getLabel()), Arrays.asList(cmt.getCmt_segment()));
			} else {
				System.out.println("Actual: " + cmt.getLabel() + " Prediction: "
						+ bayes.classify(Arrays.asList(cmt.getCmt_segment())).getCategory());
			}
		}

	}

}
