package ptit.nhunh.weka;

import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class test2{
	public static void main(String args[]) throws Exception{
		DataSource source = new DataSource("arff200.arff");
		Instances trainDataset = source.getDataSet();	
		trainDataset.setClassIndex(trainDataset.numAttributes()-1);

		LibSVM libSvm = new LibSVM();
		libSvm.buildClassifier(trainDataset);
		System.out.println(libSvm);

		DataSource source1 = new DataSource("arff200.arff");
		Instances testDataset = source1.getDataSet();	
		testDataset.setClassIndex(testDataset.numAttributes()-1);

		System.out.println("===================");
		System.out.println("Actual Class, SMO Predicted");
		for (int i = 0; i < testDataset.numInstances(); i++) {
			//get class double value for current instance
			double actualValue = testDataset.instance(i).classValue();

			//get Instance object of current instance
			Instance newInst = testDataset.instance(i);
			//call classifyInstance, which returns a double value for the class
			double predSMO = libSvm.classifyInstance(newInst);

			System.out.println(actualValue+", "+predSMO);
		}

		/*
		//load training dataset
		DataSource source = new DataSource("/home/likewise-open/ACADEMIC/csstnns/Desktop/iris.arff");
		Instances trainDataset = source.getDataSet();	
		//set class index to the last attribute
		trainDataset.setClassIndex(trainDataset.numAttributes()-1);
		//get number of classes
		int numClasses = trainDataset.numClasses();
		//print out class values in the training dataset
		for(int i = 0; i < numClasses; i++){
			//get class string value using the class index
			String classValue = trainDataset.classAttribute().value(i);
			System.out.println("Class Value "+i+" is " + classValue);
		}
		//create and build the classifier
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(trainDataset);
		//load new dataset
		DataSource source1 = new DataSource("/home/likewise-open/ACADEMIC/csstnns/Desktop/iris-unknown.arff");
		Instances testDataset = source1.getDataSet();	
		//set class index to the last attribute
		testDataset.setClassIndex(testDataset.numAttributes()-1);
		//loop through the new dataset and make predictions
		System.out.println("===================");
		System.out.println("Actual Class, NB Predicted");
		for (int i = 0; i < testDataset.numInstances(); i++) {
			//get class double value for current instance
			double actualClass = testDataset.instance(i).classValue();
			//get class string value using the class index using the class's int value
			String actual = testDataset.classAttribute().value((int) actualClass);
			//get Instance object of current instance
			Instance newInst = testDataset.instance(i);
			//call classifyInstance, which returns a double value for the class
			double predNB = nb.classifyInstance(newInst);
			//use this value to get string value of the predicted class
			String predString = testDataset.classAttribute().value((int) predNB);
			System.out.println(actual+", "+predString);
		}
		 */
	}
}