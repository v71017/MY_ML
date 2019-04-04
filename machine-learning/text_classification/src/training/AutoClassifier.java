package training;

import org.apache.commons.io.FileUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Debug;
import weka.core.FastVector;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dewadkar on 8/3/2016.
 */
public class AutoClassifier {


    private static String DATA_SET_FILE_PATH = "resources/weather.txt";

    private static final Classifier[] CLASSIFIERS = new Classifier[]{
//            new J48(), // a decision tree
            new PART(),
            new DecisionTable(),//decision table majority classifier
            new DecisionStump() //one-level decision tree
    };

    private BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;
        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }
        return inputReader;
    }

    private Evaluation classify(Classifier model,
                               Instances trainingSet, Instances testingSet) throws Exception {
        Evaluation evaluation = new Evaluation(trainingSet);
        model.buildClassifier(trainingSet);
        Debug.saveToFile("modelName",model);
        evaluation.evaluateModel(model, testingSet);
        return evaluation;
    }

    public double calculateAccuracy(FastVector predictions) {
        double correct = 0;
        for (int prediction = 0; prediction < predictions.size(); prediction++) {
            NominalPrediction nominalPrediction = (NominalPrediction) predictions.elementAt(prediction);
            if (nominalPrediction.predicted() == nominalPrediction.actual()) {
                correct++;
            }
        }

        return 100 * correct / predictions.size();
    }

    public Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
        Instances[][] split = new Instances[2][numberOfFolds];

        for (int foldNumber = 0; foldNumber < numberOfFolds; foldNumber++) {
            split[0][foldNumber] = data.trainCV(numberOfFolds, foldNumber);
            split[1][foldNumber] = data.testCV(numberOfFolds, foldNumber);
        }
        return split;
    }

    public Instances[][] dataInstances(String dataFilePath) throws IOException {
        Instances instanceData = data(dataFilePath);
        // Do 10-split cross validation
        return crossValidationSplit(instanceData, 10);
    }

    private Instances data(String dataFilePath) throws IOException {
        BufferedReader datafile = readDataFile(dataFilePath);
        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    public void validateClassifiersForTrainingData(Instances[][] dataSets, Classifier[] models) throws Exception {
        for (int dataSetType = 0; dataSetType < dataSets.length; dataSetType++)
            modelValidation(dataSets, models[dataSetType]);
    }

    private void modelValidation(Instances[][] split, Classifier model) throws Exception {
        FastVector predictions = new FastVector();
        for (int i = 0; i < split[0].length; i++) {
            Evaluation validation = classify(model, split[0][i], split[1][i]);
            predictions.appendElements(validation.predictions());
        }
        double accuracy = calculateAccuracy(predictions);

        System.out.println("Accuracy of " + model.getClass().getSimpleName() + ": "
                + String.format("%.2f%%", accuracy)
                + "\n---------------------------------");
    }

//    public static void main(String[] args) throws Exception {
//        AutoClassifier autoClassifier = new AutoClassifier();
//        String trainingFileName="C:\\Users\\IBM_ADMIN\\IdeaProjects\\nlp\\resources\\dataset\\training.arff";
//        Instances[][] split = autoClassifier.dataInstances(trainingFileName);
//        autoClassifier.validateClassifiersForTrainingData(split, CLASSIFIERS);
//
//    }

    public static void main(String[] args) throws Exception {
        AutoClassifier autoClassifier = new AutoClassifier();
        String trainingFileName=DATA_SET_FILE_PATH;
        Instances[][] split = autoClassifier.dataInstances(trainingFileName);
        autoClassifier.validateClassifiersForTrainingData(split, CLASSIFIERS);

    }

}