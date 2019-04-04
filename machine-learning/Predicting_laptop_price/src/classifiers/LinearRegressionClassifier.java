package classifiers;

import util.Util;
import weka.classifiers.functions.LinearRegression;

public class LinearRegressionClassifier {
	private LinearRegression model = new LinearRegression();

	public void LinearRegression() {
		System.out.println("LinearRegression");
		try {
			model.buildClassifier(Util.getInstance().getTrainigDataset());
			Util.getInstance().evaluateToString(model);
			System.out.println(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
