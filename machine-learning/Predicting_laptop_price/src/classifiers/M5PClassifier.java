package classifiers;

import util.Util;
import weka.classifiers.trees.M5P;

public class M5PClassifier {
	M5P m5p = new M5P();

	public void m5p() {
		System.out.println("M5P");
		try {
			m5p.buildClassifier(Util.getInstance().getTrainigDataset());
			Util.getInstance().evaluateToString(m5p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
