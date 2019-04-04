package salimm.benchmark.objs;

import java.util.List;

public class BenchmarkDataSingle extends BenchmarkData {
	private double features[][];
	private int labels[];

	public BenchmarkDataSingle(double[][] features, int[] labels, String name,int totalNumberOfClasses) {
		super(name,totalNumberOfClasses);
		this.features = features;
		this.labels = labels;
	}

	public int size() {
		return features.length;
	}

	public int featureSize() {
		return features[0].length;
	}

	public double[] features(int i) {
		return features[i];
	}

	public int label(int i) {
		return labels[i];
	}


	public BenchmarkDataSingle subset(List<Integer> indicies) {
		double[][] newFeatures = new double[indicies.size()][featureSize()];
		int[] newLabels = new int[indicies.size()];
		for (int i = 0; i < indicies.size(); i++) {
			newFeatures[i] = features[indicies.get(i)];
			newLabels[i] = labels[indicies.get(i)];
		}

		return new BenchmarkDataSingle(newFeatures, newLabels, getName(),getTotalNumberOfClasses());
	}

	@Override
	public String toString() {
		return "{name:" + getName() + ", type: " + getClass().getSimpleName() + ", size:" + featureSize()
				+ ", samples: " + size() + "}";
	}
}
