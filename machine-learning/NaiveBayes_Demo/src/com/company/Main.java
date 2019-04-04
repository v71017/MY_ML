package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static String getSMS() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter your message:");
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Are we referring to the array returned by distributionByInstance()? If so,
	 * then the meaning is the same for all classifiers in Weka. Each entry in
	 * the array corresponds to a class value (in the order that the class
	 * values are declared in the header of the data). Each entry holds the
	 * predicted probability for the corresponding class label. They should sum
	 * to 1. 
	 */
	public static void main(String[] args) throws Exception {
		Learner learner = new Learner();
		learner.loadDataset("data/spam_training.arff");
		learner.evaluate();
		learner.learn();

		SpamClassifier spamClassifier = new SpamClassifier(getSMS());
		spamClassifier.makeInstance();
		int res = spamClassifier.classify(learner.getClassifier());
		System.out.println("Class result:" + res);
	}
}
