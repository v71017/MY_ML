package com.naivebayes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class EmNaiveBayesTest {

	private List<String> X = new ArrayList<String>();
	private List<String> y = new ArrayList<String>();

	public void setUp() throws IOException {
		X.add("BIGIP_GTM_SERVER");
		X.add("Network:avi");
		X.add("GTM-LTM");

		y.add("gtm");
		y.add("avi");
		y.add("ltm");

	}

	public void testTrain() throws IOException {
		setUp();
		EmNaiveBayes emNaiveBayes = new EmNaiveBayes();
		emNaiveBayes.fit(X, y);

		List<String> predictX = new ArrayList<String>();
		List<String> predictY = new ArrayList<String>();
		predictX.add("GTM-LTM");
		predictY.add("-1");

		for (int i = 0; i < predictX.size(); i++) {
			Entry<String, Double> result = emNaiveBayes.predict(predictX.get(i));
			System.out.println(result.getKey());
		}
	}

	public static void main(String args[]) {
		EmNaiveBayesTest emNaiveBayesTest = new EmNaiveBayesTest();
		try {
			emNaiveBayesTest.testTrain();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}