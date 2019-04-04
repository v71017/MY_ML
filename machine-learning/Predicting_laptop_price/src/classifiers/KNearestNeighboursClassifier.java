package classifiers;

import util.Util;
import weka.classifiers.lazy.IBk;

public class KNearestNeighboursClassifier {

	public void kNearestNeighbours(String[] options) {
		try {
			IBk ibk = new IBk();
		ibk.setOptions(options);
		System.out.println("kNearestNeighbours with " + options[0]);
		double MMSETemp = Double.MAX_VALUE;
			int[] kArray = Util.getInstance().getKArray();

			double[] meanSquredErrorsK = new double[kArray.length];
			for (int i = 0; i < kArray.length; i++) {

				ibk.buildClassifier(Util.getInstance().getTrainigDataset());
				ibk.setKNN(kArray[i]);
				meanSquredErrorsK[i] = Util.getInstance().getRootMeanSquaredError(ibk);
			}

			int k = Util.getInstance().getPositionOfMinimumMSE(meanSquredErrorsK);
			int kTemp = kArray[k];
			for (int i = kTemp - 4; i < kTemp + 4; i++) {
				ibk.setKNN(i);
				if (MMSETemp > Util.getInstance().getRootMeanSquaredError(ibk)) {
					Util.getInstance().setkOptimum(i);
				}
			}

			System.out.println("Optimalno k je " + Util.getInstance().getkOptimum());
			ibk.setKNN(Util.getInstance().getkOptimum());
			Util.getInstance().evaluateToString(ibk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
