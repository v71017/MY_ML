package util;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Util {
	public static Util instance;

	private Instances instances;
	private Instances trainigDataset;
	private Instances testDataset;
	private int trainSizePercent = 90;
	private int testSize;
	private Instance predictingInstance;
	private double price;
	private int kOptimum;

	public int getkOptimum() {
		return kOptimum;
	}

	public void setkOptimum(int kOptimum) {
		this.kOptimum = kOptimum;
	}

	private Util() {

	}

	public static Util getInstance() {
		if (instance == null) {
			instance = new Util();
		}
		return instance;
	}

	public void buildDatasets(String fileLocation) {
		DataSource source;
		try {
			source = new DataSource(fileLocation);
			instances = source.getDataSet();

			Instances[] trainingTestDataset = buildTraingAndTestSets(instances);
			trainigDataset = trainingTestDataset[0];
			testDataset = trainingTestDataset[1];

			predictingInstance = testDataset.get(Math.round(new Random().nextInt(testDataset.size())));
			price = predictingInstance.classValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Evaluation evaluate(Classifier classifier, Instances testSet) {
		Evaluation e = null;
		try {
			e = new Evaluation(trainigDataset);
			e.crossValidateModel(classifier, testSet, 10, new Random(1));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return e;
	}

	public void evaluateToString(Classifier classifier, Instances testSet) {
		Evaluation e = evaluate(classifier, testSet);
		double predictPrice;
		try {
			predictPrice = classifier.classifyInstance(predictingInstance);
			System.out.println(predictPrice);
			System.out.println(Math.abs(predictPrice - price));
			System.out.println(e.toSummaryString(true));
			// return new String[] { predictPrice + "", Math.abs(predictPrice -
			// price) + "", e.toSummaryString(true) };
		} catch (Exception e1) {
			e1.printStackTrace();
			// return null;
		}
	}

	public void evaluateToString(Classifier classifier) {
		evaluateToString(classifier, testDataset);
	}

	public double getRootMeanSquaredError(Classifier classifier) {
		Evaluation e = evaluate(classifier, testDataset);
		return e.rootMeanSquaredError();
	}

	public Instances getTrainigDataset() {
		return trainigDataset;
	}

	public void setTrainigDataset(Instances trainigDataset) {
		this.trainigDataset = trainigDataset;
	}

	public int getTrainSizePercent() {
		return trainSizePercent;
	}

	public void setTrainSizePercent(int trainSizePercent) {
		this.trainSizePercent = trainSizePercent;
	}

	public Instances getInstances() {
		return instances;
	}

	public Instances getTestDataset() {
		return testDataset;
	}

	public int getTestSize() {
		return testSize;
	}

	public Instance getPredictingInstance() {
		return predictingInstance;
	}

	public double getPrice() {
		return price;
	}

	// return int array from 5 to number of instances by 5
	public int[] getKArray() {
		int length = instances.numInstances() / 5;
		int[] kArray = new int[length - 1];
		kArray[0] = 5;
		for (int i = 1; i < kArray.length; i++) {
			kArray[i] = i * 5 + 5;
		}

		return kArray;
	}

	// return position of minimun value from array
	public int getPositionOfMinimumMSE(double[] meanSquredErrors) {
		double maxMSE = meanSquredErrors[0];
		int position = 0;

		for (int i = 1; i < meanSquredErrors.length; i++) {
			if (meanSquredErrors[i] > maxMSE) {
				maxMSE = meanSquredErrors[i];
				position = i;
			}
		}
		return position;
	}

	public Instances[] buildTraingAndTestSets(Instances instances) {
		instances.randomize(new java.util.Random(0));
		int trainSize = (int) Math.round(instances.numInstances() * trainSizePercent / 100);
		testSize = instances.numInstances() - trainSize;
		Instances trainigDataset = new Instances(instances, 0, trainSize);
		Instances testDataset = new Instances(instances, trainSize, testSize);
		trainigDataset.setClassIndex(trainigDataset.numAttributes() - 1);
		testDataset.setClassIndex(testDataset.numAttributes() - 1);
		return new Instances[] { trainigDataset, testDataset };
	}

	public void print(String[] results) {
		for (String s : results) {
			System.out.println(s);
		}
	}
}
