package test;

import classifiers.KNearestNeighboursClassifier;
import classifiers.LinearRegressionClassifier;
import classifiers.LinearRegression_NearestNeighborClassifier;
import classifiers.M5PClassifier;
import classifiers.REPTreeClassifier;
import util.Util;

public class Test {

	public static void main(String[] args) {
		Util.getInstance().buildDatasets("datasets/Laptops0806.arff");

		LinearRegressionClassifier lrc = new LinearRegressionClassifier();
		lrc.LinearRegression();

		KNearestNeighboursClassifier knnc = new KNearestNeighboursClassifier();
		knnc.kNearestNeighbours(new String[] { "-I" });
		knnc.kNearestNeighbours(new String[] { "-F" });

		REPTreeClassifier rtc = new REPTreeClassifier();
		rtc.repTree();

		M5PClassifier m5pc = new M5PClassifier();
		m5pc.m5p();

		LinearRegression_NearestNeighborClassifier lrnnc = new LinearRegression_NearestNeighborClassifier();
		lrnnc.LinearRegression_NearestNeighbor();

		Util.getInstance().buildDatasets("datasets/Laptops20806.arff");

		lrc.LinearRegression();

		knnc.kNearestNeighbours(new String[] { "-I" });
		knnc.kNearestNeighbours(new String[] { "-F" });

		rtc.repTree();

		m5pc.m5p();

		lrnnc.LinearRegression_NearestNeighbor();
	}
}
