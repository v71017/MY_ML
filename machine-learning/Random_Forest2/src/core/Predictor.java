package core;

import java.io.BufferedReader;
import java.io.FileReader;

import dataClassifiers.BaggingClassifier;
import dataClassifiers.DecisionTreeClassifier;
import dataClassifiers.NaiveBayesClassifier;
import dataClassifiers.RandomForestClassifier;
import dataClassifiers.StackingClassifier;
import weka.core.Instances;

public class Predictor {

	// Enum of classifier type
	public static enum classifierType {
		NAIVEBAYES, DECISION_TREE, RANDOM_FOREST, BAGGING, STACKING
	}

	// Flag to predict for data with missing "day" value
	// I.E. test data set
	final private static boolean evaluateAgainstSelf = false;

	// Disable verbose output
	final static boolean VERBOSE = false;
	
	// Set Classifiers to run
	final private static classifierType[] evalutionQueue = {
			classifierType.NAIVEBAYES,
			classifierType.DECISION_TREE,
			classifierType.RANDOM_FOREST };
	
	final private static int startYear = -1; 

	// ///////////////////////////////////////////////

	// Location of training and test data
	private static String trainingInput = "datasets/melb.train.arff";
	private static String testInput = "datasets/melb.test.arff";

	// Training data
	private static Instances trainingData;
	private static Instances testData;

	public static void main(String[] args) {

		// Build data sets
		buildTrainingDataSet();
		buildTestDataSet();

		// Run evaluation
		runClassifierofChoice(evalutionQueue);
	}

	private static void runClassifierofChoice(classifierType[] queue) {
		// Build evaluator
		Evaluator eval = new Evaluator(trainingData);

		for (classifierType cType : queue) {

			// Start evaluation timer
			long startTime = System.currentTimeMillis();

			// Use specified classifier
			if (cType == classifierType.NAIVEBAYES) {

				System.out.println("Classifier used: NaiveBayes Classifer");

				// Build classifier
				NaiveBayesClassifier nb = new NaiveBayesClassifier(trainingData);

				if (evaluateAgainstSelf) {
					// Evaluate
					eval.doEvaluation(nb.getClassifier(), trainingData);
				} else {
					// Evaluate
					eval.doEvaluation(nb.getClassifier(), testData);
					
					// Calculate time taken
					double timeTaken = (System.currentTimeMillis() - startTime);
					timeTaken = timeTaken/ 1000;

					// Write to file
					String[] outputPathSegments = testInput.split("/");
					eval.writePredictionsToFile("outputs/"
							+ outputPathSegments[outputPathSegments.length - 1]
							+ ".NaiveBayes.output", testData, "Time taken: "
							+ timeTaken);
				}

			} else if (cType == classifierType.DECISION_TREE) {

				System.out.println("Classifier used: Decision Trees");

				// Build classifier
				DecisionTreeClassifier dt = new DecisionTreeClassifier(trainingData);

				if (evaluateAgainstSelf) {
					// Evaluate
					eval.doEvaluation(dt.getClassifier(), trainingData);
				} else {
					// Evaluate
					eval.doEvaluation(dt.getClassifier(), testData);
					
					// Calculate time taken
					double timeTaken = (System.currentTimeMillis() - startTime);
					timeTaken = timeTaken/ 1000;

					// Write to file
					String[] outputPathSegments = testInput.split("/");
					eval.writePredictionsToFile("outputs/"
							+ outputPathSegments[outputPathSegments.length - 1]
							+ ".DecisionTree.output", testData, "Time taken: "
							+ timeTaken);
				}

			} else if (cType == classifierType.BAGGING) {
				System.out
						.println("Classifier used: Bagging (Used with Decision Trees)");

				// Build classifier
				BaggingClassifier bag = new BaggingClassifier(trainingData);

				if (evaluateAgainstSelf) {
					// Evaluate
					eval.doEvaluation(bag.getClassifier(), trainingData);
				} else {
					// Evaluate
					eval.doEvaluation(bag.getClassifier(), testData);

					// Calculate time taken
					double timeTaken = (System.currentTimeMillis() - startTime);
					timeTaken = timeTaken/ 1000;
					
					// Write to file
					String[] outputPathSegments = testInput.split("/");
					eval.writePredictionsToFile("outputs/"
							+ outputPathSegments[outputPathSegments.length - 1]
							+ ".Bagging.output", testData,
							"Time taken: " + timeTaken);
				}

			} else if (cType == classifierType.STACKING) {
				System.out
						.println("Classifier used: An aggregate of NaiveBayes and Decision Trees\n");

				classifierType[] cl = { classifierType.NAIVEBAYES,
						classifierType.DECISION_TREE };

				// Build classifier
				StackingClassifier stack = new StackingClassifier(trainingData, cl);

				if (evaluateAgainstSelf) {
					// Evaluate
					eval.doEvaluation(stack.getClassifier(), trainingData);
				} else {
					// Evaluate
					eval.doEvaluation(stack.getClassifier(), testData);
					
					// Calculate time taken
					double timeTaken = (System.currentTimeMillis() - startTime);
					timeTaken = timeTaken/ 1000;
					
					// Write to file
					String[] outputPathSegments = testInput.split("/");
					eval.writePredictionsToFile("outputs/"
							+ outputPathSegments[outputPathSegments.length - 1]
							+ ".Stacking.output", testData, "Time taken: "
							+ timeTaken);
				}

			} else {

				System.out.println("Classifier used: Random Forest\n");

				// Build classifier
				RandomForestClassifier rf = new RandomForestClassifier(trainingData);

				//System.out.println(rf.getClassifier().toString());

				if (evaluateAgainstSelf) {
					eval.doEvaluation(rf.getClassifier(), trainingData);
				} else {
					// Evaluate
					eval.doEvaluation(rf.getClassifier(), testData);

					// Calculate time taken
					double timeTaken = (System.currentTimeMillis() - startTime);
					timeTaken = timeTaken/ 1000;
					
					// Write to file
					String[] outputPathSegments = testInput.split("/");
					eval.writePredictionsToFile("outputs/"
							+ outputPathSegments[outputPathSegments.length - 1]
							+ ".RandomForest.output", testData, "Time taken: "
							+ timeTaken);
				}
			}

			// Print out stats
			if (VERBOSE) {
				System.out.println(eval.getEvaluationStats());
				System.out.println(eval.confusionMatrixToString());
			}
		}
	}

	private static void buildTestDataSet() {
		System.out.print("Reading in test data... ");

		try {

			// read from test data source
			BufferedReader reader = new BufferedReader(
					new FileReader(testInput));
			testData = new Instances(reader);
			
			// Use data only from a certain year
			if (startYear > 1855) {
				for (int i = 0; i < testData.numInstances(); i++) {
					
					if (testData.instance(i).value(1) < startYear) {
						testData.delete(i);
					}
				}

			}

			if (testData.classIndex() == -1)
				testData.setClassIndex(testData.numAttributes() - 1);

		} catch (Exception e) {
			System.err.print("Unable to build instances from training input.\n"
					+ "Exiting Program.");
			System.exit(1);
		}

		System.out.println("Done!\n");
	}

	private static void buildTrainingDataSet() {
		System.out.print("Reading in training data... ");

		try {

			// Build training data set
			BufferedReader reader = new BufferedReader(new FileReader(
					trainingInput));
			trainingData = new Instances(reader);

			
			
			// Delete instances with missing data
			for (int i = 1; i < trainingData.numAttributes(); i++) {
				trainingData.deleteWithMissing(i);
			}

			// Use data only from a certain year
			if (startYear > 1855) {
				for (int i = 0; i < trainingData.numInstances(); i++) {
					
					if (trainingData.instance(i).value(1) < startYear) {
						trainingData.delete(i);
					}
				}

			}

			
			if (trainingData.classIndex() == -1)
				trainingData.setClassIndex(trainingData.numAttributes() - 1);

		} catch (Exception e) {
			System.err.print("Unable to build instances from training input.\n"
					+ "Exiting Program.");
			System.exit(1);
		}

		System.out.println("Done!\n");
	}


}
