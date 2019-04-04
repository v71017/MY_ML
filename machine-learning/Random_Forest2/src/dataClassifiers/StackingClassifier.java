package dataClassifiers;

import core.Predictor;
import core.Predictor.classifierType;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.Stacking;
import weka.core.Instances;

public class StackingClassifier {

	Stacking stackingClassifier = null;
	Classifier[] classifierList = null;
	
	public StackingClassifier(Instances trainingData, Predictor.classifierType[] cl) {
		
		
		try {
			stackingClassifier = new Stacking();
			
			classifierList = new Classifier[cl.length];
			
			// Set classifiers
			for (int i = 0; i < cl.length; i++) {
				if (cl[i] == classifierType.NAIVEBAYES) {
					classifierList[i] = new NaiveBayesClassifier(trainingData).getClassifier();
				} 
				else if (cl[i] == classifierType.DECISION_TREE) {
					classifierList[i] = new DecisionTreeClassifier(trainingData).getClassifier();
				}
			}
			
			// Sets the number of cross-validation folds.
			String[] options = weka.core.Utils.splitOptions("-X 3");
			stackingClassifier.setOptions(options);
			
			stackingClassifier.setClassifiers(classifierList);
			stackingClassifier.setMetaClassifier(new NaiveBayes());
			stackingClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public Classifier getClassifier() {
		return stackingClassifier;
	}
}
