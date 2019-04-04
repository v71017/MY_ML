package classifiers;

import util.Util;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

public class LinearRegression_NearestNeighborClassifier {
	LinearRegression lRNN = new LinearRegression();

	public void LinearRegression_NearestNeighbor() {
		System.out.println("LinearRegression_NearestNeighbor");
		LinearNNSearch search = new LinearNNSearch(Util.getInstance().getInstances());
		Instances reducedInstancesWithTest;
		Instances reducedInstancesTraining;

		try {

			reducedInstancesWithTest = search.kNearestNeighbours(Util.getInstance().getPredictingInstance(),
					Util.getInstance().getkOptimum());
			reducedInstancesWithTest.setClassIndex(reducedInstancesWithTest.numAttributes() - 1);
			reducedInstancesTraining = Util.getInstance().buildTraingAndTestSets(reducedInstancesWithTest)[0];

			lRNN.buildClassifier(reducedInstancesTraining);
			Util.getInstance().evaluateToString(lRNN, reducedInstancesWithTest);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
