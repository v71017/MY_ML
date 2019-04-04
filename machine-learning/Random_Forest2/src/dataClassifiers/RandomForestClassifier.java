package dataClassifiers;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class RandomForestClassifier {

	Classifier rfClassifier = null;
	
	public RandomForestClassifier(Instances trainingData) {
		try {
			rfClassifier = new RandomForest();
			
			// Set the number of features to consider
			String[] options = weka.core.Utils.splitOptions("-K 6 -I 13");
			
			rfClassifier.setOptions(options);
			
			rfClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Classifier getClassifier() {
		return rfClassifier;
	}
}
