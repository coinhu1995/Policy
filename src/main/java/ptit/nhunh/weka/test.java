package ptit.nhunh.weka;

import java.io.File;
import java.io.FileInputStream;

import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class test {
	public static void main(String[] args) throws Exception {
		new test().execute();
	}

	private void execute() throws Exception {
		DataSource sourceTrain = new DataSource(new FileInputStream(new File("t.arff")));
		DataSource sourceTest = new DataSource(new FileInputStream(new File("t1.arff")));
		Instances dataTrain = sourceTrain.getDataSet();
		Instances dataTest = sourceTest.getDataSet();

		dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
		dataTest.setClassIndex(dataTest.numAttributes() - 1);

		// initialize svm classifier
		LibSVM svm = new LibSVM();
		svm.buildClassifier(dataTrain);

		for (Instance in : dataTest) {
			System.out.println(svm.classifyInstance(in));
		}

		// WekaPackageManager.loadPackages(true);
		// AbstractClassifier classifier = (AbstractClassifier)
		// Class.forName("weka.classifiers.functions.LibSVM")
		// .newInstance();
		//
		// String options = ("-S 0 -K 0 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E
		// 0.001 -P 0.1");
		// String[] optionsArray = options.split(" ");
		// classifier.setOptions(optionsArray);
		//
		// classifier.buildClassifier(dataTrain);
		//
		// for (int i = 0; i < dataTest.size(); i++) {
		// System.out.println(classifier.classifyInstance(dataTest.get(i)));
		// }
	}
}
