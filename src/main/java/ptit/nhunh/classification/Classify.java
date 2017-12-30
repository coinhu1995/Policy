package ptit.nhunh.classification;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import ca.uwo.csd.ai.nlp.kernel.KernelManager;
import ca.uwo.csd.ai.nlp.kernel.LinearKernel;
import ca.uwo.csd.ai.nlp.libsvm.svm_model;
import ca.uwo.csd.ai.nlp.libsvm.ex.Instance;
import ca.uwo.csd.ai.nlp.libsvm.ex.SVMPredictor;
import ptit.nhunh.dao.SQLDAO;
import ptit.nhunh.dao.SQLDAOFactory;
import utils.DataFileReader;

public class Classify {
	private String path;
	private SQLDAO cmtDao = SQLDAOFactory.getDAO(SQLDAOFactory.COMMENT);
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		new Classify().execute();
	}
	
	public void execute() throws ClassNotFoundException, IOException {
		System.out.println("Processing...");
		Scanner scan = new Scanner(new File("src\\main\\resources\\path.txt"));
		this.path = scan.nextLine();
		this.doLinearKernel(this.path + "data.txt");
		scan.close();
	}

	private void doLinearKernel(String testFile) throws IOException, ClassNotFoundException {

		svm_model model = SVMPredictor.loadModel("src\\main\\resources\\model\\model.txt");
		KernelManager.setCustomKernel(new LinearKernel());

		Instance[] testingInstances = DataFileReader.readDataFile(testFile);
		double[] predictions = SVMPredictor.predict(testingInstances, model, true);

		Scanner scan = new Scanner(new File(this.path + "id.txt"));
		int count = 0;
		while (scan.hasNextLine()) {
			this.cmtDao.update(
					"update TblComment set label = " + predictions[count] + " where id = " + scan.nextLine().trim());
			count++;
		}
		scan.close();
	}

}
