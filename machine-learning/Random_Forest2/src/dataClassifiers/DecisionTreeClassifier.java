package dataClassifiers;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DecisionTreeClassifier {
	
	Classifier decisionTreeClassifier = null;
	
	public DecisionTreeClassifier(Instances trainingData) {
		
		try {
			// Build NaiveBayes Classifier
			decisionTreeClassifier = new J48();
			
			// Set minimum number of instances per leaf to 6
			String[] options = weka.core.Utils.splitOptions("-M 6");
			decisionTreeClassifier.setOptions(options);
			
			decisionTreeClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public Classifier getClassifier() {
		return decisionTreeClassifier;
	}
	
}
