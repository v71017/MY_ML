README
--------------------------
Done by: Ryan Leong (395463)


Compiling
----------------
The compile script will only work on the CIS servers as it has a different directory for Java 1.6

I've written a shell script to compile and run with the default settings
./compilerun.sh

Alternatively, this is the command to compile :
/usr/java1.6/bin/javac -d bin/ -cp lib/weka.jar src/core/Evaluator.java src/core/Classifiers.java src/core/Predictor.java

Running
----------------
The command will only work on the CIS servers as it has a different directory for Java 1.6

If the shell script does not run the program.
/usr/java1.6/bin/java -Xmx128m -cp bin:lib/weka.jar core.Predictor

By default, the program will run all three classifiers. Using melb.train.arff and output the predictions of melb.test.arff.
Inputs will be from the "datasets" directory while predictions will be output to the "outputs" directory


Core Package
============
Predictor.java
	
	classifierType[] evalutionQueue

		A list of classifiers which are to be run
		By default list contains
			Naive Bayes
			C4.5 desicion tree
			Random forest

	main()

		Runs the entire program
		Builds both training and test datasets
		Run classifiers

	buildTestDataSet()

		Read in test data from file and build dataset

	buildTrainingDataSet()

		Read in training data from file and build dataset

	runClassifierofChoice()

		Runs all classifier in evaluationQueue list 

Evaluator.java

	Evaluator()

		Constructor
		Creates weka evaluator with training dataset

	confusionMatrixToString()

		Returns string with confusion matrix from the evaluation

	doEvaluation()

		Runs an evaluation with the test data set based on a given classifier

	getDayString()

		Prediction of day in output is represented as a Double value.
		Returns String that represents the Double value

	getEvaluationStats()

		Returns string with all stats from the evaluation, such as accuracy rate

	getEvaluator()

		Returns evaluation object

	getPredictions()

		Returns a list of predictions for each instance in the test dataset

	printPredictions()

		// need?

	writePredictionsToFile()

		Write predicitons for test data into an ouptut file

dataclassifiers Package
========================

NaiveBayesClassier.java

	NaiveBayesClassifier()

		Builds a Naive Bayes classifier with the training dataset

	getClassifier()

		Returns Naive Bayes classifier object

DecisionTreeClassier.java

	DecisionTreeClassifier()

		Builds a C4.5 decision tree  with the training dataset
		A minimum of 6 nodes per leaf was set for the tree

	getClassifier()

		Returns C4.5 decision tree object

RandomForestClassifier.java
	
	RandomForestClassifier()

		Builds a Random Forest classifier with the training dataset
		With a m value of 6 and 13 trees

	getClassifier()

		Returns Random Forest classifier object

Unimplemented classifiers
-------------------------
BaggingClassifier.java

	BaggingClassifier()

		Builds a Bagging classifier with the training dataset
		Percentage of training size set to 90%

	getClassifier()

		Returns Bagging classifier object

StackingClassifier.java

	StackingClassifier()

		Builds a Stacking classifier with the training dataset
		Which uses a Naive Bayes classifier and C4.5 Decision tree

	getClassifier()

		Returns Stacking classifier object


