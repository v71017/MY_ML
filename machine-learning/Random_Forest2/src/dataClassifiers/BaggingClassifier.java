package dataClassifiers;

import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;

public class BaggingClassifier {

	Classifier baggingClassifier = null;
	
	public BaggingClassifier(Instances trainingData) {
		try {
			baggingClassifier = new Bagging();
			
			// Size of each bag, as a percentage of the training size (default 100).
			String[] options = weka.core.Utils.splitOptions("-P 80");
			
			baggingClassifier.setOptions(options);
			baggingClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public Classifier getClassifier() {
		return baggingClassifier;
	}
	
}
