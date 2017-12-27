package ptit.nhunh.classification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import ca.uwo.csd.ai.nlp.kernel.KernelManager;
import ca.uwo.csd.ai.nlp.kernel.LinearKernel;
import ca.uwo.csd.ai.nlp.libsvm.svm_model;
import ca.uwo.csd.ai.nlp.libsvm.ex.Instance;
import ca.uwo.csd.ai.nlp.libsvm.ex.SVMPredictor;
import utils.DataFileReader;

public class Classify {

	public void execute() throws ClassNotFoundException, IOException {
		System.out.println("Processing...");
		Scanner scan = new Scanner(new File("src\\main\\resources\\path.txt"));
		String path = scan.nextLine();
		new Classify().LinearKernel(path + "data.arff", path + "output.txt");
		scan.close();
	}

	private void LinearKernel(String testFile, String outFile) throws IOException, ClassNotFoundException {

		svm_model model = SVMPredictor.loadModel("src\\main\\resources\\model\\model.txt");
		KernelManager.setCustomKernel(new LinearKernel());

		Instance[] testingInstances = DataFileReader.readDataFile(testFile);
		double[] predictions = SVMPredictor.predict(testingInstances, model, true);
		this.writeOutputs(outFile, predictions);
	}

	private void writeOutputs(String outputFileName, double[] predictions) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		for (double p : predictions) {
			writer.write(String.format("%.0f\n", p));
		}
		writer.close();
	}
}
