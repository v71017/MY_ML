package dataClassifiers;


import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class NaiveBayesClassifier {

	Classifier naiveBayesClassifier = null;
	
	// Constructor
	public NaiveBayesClassifier(Instances trainingData) {
		
		try {
			// Build NaiveBayes Classifier
			naiveBayesClassifier = new NaiveBayes();
			naiveBayesClassifier.buildClassifier(trainingData);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Classifier getClassifier(){
		return naiveBayesClassifier;
	}
}
