package ptit.nhunh.weka;

import java.sql.Timestamp;

public class test {
	public static void main(String[] args) throws Exception {
		new test().execute();
	}

	private void execute() throws Exception {
//		DataSource sourceTrain = new DataSource(new FileInputStream(new File("arff200.arff")));
//		DataSource sourceTest = new DataSource(new FileInputStream(new File("arff100.arff")));
//		Instances dataTrain = sourceTrain.getDataSet();
//		Instances dataTest = sourceTest.getDataSet();
//
//		dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
//		dataTest.setClassIndex(dataTest.numAttributes() - 1);
//
//		// initialize svm classifier
//		LibSVM svm = new LibSVM();
//		svm.setKernelType(new SelectedTag(svm.KERNELTYPE_LINEAR, svm.TAGS_KERNELTYPE));
//		svm.buildClassifier(dataTrain);
//
//		for (Instance in : dataTest) {
//			System.out.println(svm.classifyInstance(in));
//		}
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		System.out.println(ts);
		System.out.println(new Timestamp(ts.getTime() - 3600000));
	}
}
