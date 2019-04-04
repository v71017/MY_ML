package com.mavi.cagatay;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

/**
 * Created by cyurtoz on 07/05/17.
 */
public class FindWhoWillLeave {
	public static void main(String[] args) throws Exception {
		final InputStream resourceAsStream = new FileInputStream("/Users/vpati/Intuit_project/ml/rf/HR-Analytics/src/com/mavi/cagatay/HR_comma_sep.arff");    
		final NaiveBayes nb = new NaiveBayes();
		final Logistic logistic = new Logistic();
		logistic.setMaxIts(100);

		final Instances instancesNew = new Instances(new InputStreamReader(resourceAsStream));
		instancesNew.setClassIndex(6);


		logistic.buildClassifier(instancesNew);
		nb.buildClassifier(instancesNew);

		PriorityQueue<ExtendedInstance> logisticQueue = createQueue(logistic);

		PriorityQueue<ExtendedInstance> nbQueue = createQueue(nb);

		process(instancesNew, nb, nbQueue);
		process(instancesNew, logistic, logisticQueue);
		printQueue(logisticQueue);
		System.out.println("-");
		printQueue(nbQueue);


	}

	private static PriorityQueue<ExtendedInstance> createQueue(final Classifier clz) {
		return new PriorityQueue<>((o1, o2) -> {
			try {
				final double v1 = clz.distributionForInstance(o1.getInstance())[1];
				final double v2 = clz.distributionForInstance(o2.getInstance())[1];
				return Double.compare(v1, v2);
			} catch (Exception e) {
				return 0;
			}
		});
	}

	private static void printQueue(PriorityQueue<ExtendedInstance> logisticQueue) {

		for (ExtendedInstance i : logisticQueue) {
			System.out.format("%6d %f %4f %20s", i.getNumber(), i.getClassValue(), i.getDistribution(), i
					.getInstance()
					.toString());
			System.out.print("\n");
		}
	}

	private static void process(Instances instancesNew, Classifier classifier, PriorityQueue<ExtendedInstance>
			filtered) throws Exception {
		for (int i = 0; i < instancesNew.numInstances(); i++) {
			final Instance instance = instancesNew.instance(i);
			final double value = instance.classValue();
			if (value == 0) {
				filtered.add(new ExtendedInstance(instance, value, i, classifier.distributionForInstance(instance)[1]));
				if (filtered.size() > 10) {
					filtered.poll();
				}
			} else {
				System.out.print("");
			}
		}
	}


	public static class ExtendedInstance {
		Instance instance;
		double classValue;
		int number;
		private double distribution;


		public ExtendedInstance(Instance instance, double classValue, int number, double v) {
			this.instance = instance;
			this.classValue = classValue;
			this.number = number;
			distribution = v;
		}

		public Instance getInstance() {
			return instance;
		}

		public double getClassValue() {
			return classValue;
		}

		public int getNumber() {
			return number;
		}

		public double getDistribution() {
			return distribution;
		}
	}
	
}
