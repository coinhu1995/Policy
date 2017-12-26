/**
 * 
 */
package ptit.nhunh.classification;

import java.io.IOException;

import ca.uwo.csd.ai.nlp.kernel.KernelManager;
import ca.uwo.csd.ai.nlp.kernel.LinearKernel;
import ca.uwo.csd.ai.nlp.libsvm.svm_model;
import ca.uwo.csd.ai.nlp.libsvm.ex.Instance;
import ca.uwo.csd.ai.nlp.libsvm.ex.SVMPredictor;
import utils.DataFileReader;

/**
 * @author coinh
 *
 *         Dec 22, 2017
 */
public class test {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		new test().LinearKernel("2 20:2", "output");
	}

	public void LinearKernel(String input, String outputFile) throws IOException, ClassNotFoundException {
		svm_model model = SVMPredictor.loadModel("model.txt");
		KernelManager.setCustomKernel(new LinearKernel());

		Instance testingInstance = DataFileReader.readDataString(input);
		double prediction = SVMPredictor.predict(testingInstance, model, true);
		System.out.println(prediction);
	}
}
