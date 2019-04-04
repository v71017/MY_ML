package classifiers;

import util.Util;
import weka.classifiers.trees.REPTree;

public class REPTreeClassifier {
	private REPTree rt = new REPTree();

	public void repTree() {
		System.out.println("repTree");
		try {
			rt.buildClassifier(Util.getInstance().getTrainigDataset());
			Util.getInstance().evaluateToString(rt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
