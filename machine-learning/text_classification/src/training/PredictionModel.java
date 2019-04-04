package training;

import org.apache.commons.io.FileUtils;
import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.TextDirectoryLoader;
import weka.core.stemmers.SnowballStemmer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dewadkar on 8/7/2016.
 */
public class PredictionModel {

    public static String LABEL_TRAIN_DATA_SET_DIR_PATH = "resources/dataset/label/training/";
    public static String LABEL_TEST_DATA_SET_DIR_PATH = "resources/dataset/label/testing";
    public static String PRODUCT_TRAIN_DATA_SET_DIR_PATH = "resources/dataset/product/training/";
    public static String PRODUCT_TEST_DATA_SET_DIR_PATH = "resources/dataset/product/testing";


    public static void main(String[] args) throws Exception {

        PredictionModel predictionModel = new PredictionModel();
        System.out.println("Wait ..  Converting directory into Data set");
        // convert the directory into a dataset
//        DataSet securlyDataSet = new DataSet();
//        securlyDataSet.createTrainFileToDataSetDirForCar();
//        securlyDataSet.createTestFileToDataSetDirForCar();
//        securlyDataSet.createTrainFileToDataSetDirForCarRecommendation();
//        securlyDataSet.createTestFileToDataSetDirForCarRecommandation();

        System.out.println("Done ..  Created data set directory.");


//        createDataSetForLabel(predictionModel);
        createDataSetForProduct(predictionModel);

    }

    private static void createDataSetForProduct(PredictionModel predictionModel) throws Exception {
        System.out.println("Wait ..  loading training dataset");
        Instances trainingDataSet = predictionModel.loadDirDataSetToWekaFormat(PRODUCT_TRAIN_DATA_SET_DIR_PATH);

        Instances testingDataSet = predictionModel.loadDirDataSetToWekaFormat(PRODUCT_TEST_DATA_SET_DIR_PATH);

        System.out.println("Done ..  loaded dataset");

        System.out.println("Wait ..  converting data set to word vectors.");
        Filter stringToWordVectorFilter = predictionModel.filterStringToWordVectorType(trainingDataSet);
        Instances filteredTrainingDataSet = Filter.useFilter(trainingDataSet, stringToWordVectorFilter);
        System.out.println("Done ..  word vector file like arff file of weka created.");

        ArffSaver saver = new ArffSaver();
        saver.setInstances(filteredTrainingDataSet);
        saver.setFile(new File("resources/product/train.arff"));
        saver.writeBatch();

        System.out.println("Done ..  train file persisted.");

        System.out.println("Wait ..  converting test data set to word vectors.");

        Instances filteredTestingDataSet = Filter.useFilter(testingDataSet, stringToWordVectorFilter);
        ArffSaver saveTestFile = new ArffSaver();
        saveTestFile.setInstances(filteredTestingDataSet);
        saveTestFile.setFile(new File("resources/product/test.arff"));
        saveTestFile.writeBatch();
        System.out.println("Done ..  test file persisted.");

    }

    private static void createDataSetForLabel(PredictionModel predictionModel) throws Exception {
        System.out.println("Wait ..  loading training dataset");
        Instances trainingDataSet = predictionModel.loadDirDataSetToWekaFormat(LABEL_TRAIN_DATA_SET_DIR_PATH);

        Instances testingDataSet = predictionModel.loadDirDataSetToWekaFormat(LABEL_TEST_DATA_SET_DIR_PATH);

        System.out.println("Done ..  loaded dataset");

        System.out.println("Wait ..  converting data set to word vectors.");
        Filter stringToWordVectorFilter = predictionModel.filterStringToWordVectorType(trainingDataSet);
        Instances filteredTrainingDataSet = Filter.useFilter(trainingDataSet, stringToWordVectorFilter);
        System.out.println("Done ..  word vector file like arff file of weka created.");

        ArffSaver saver = new ArffSaver();
        saver.setInstances(filteredTrainingDataSet);
        saver.setFile(new File("resources/label/train.arff"));
        saver.writeBatch();

        System.out.println("Done ..  train file persisted.");

        System.out.println("Wait ..  converting test data set to word vectors.");

        Instances filteredTestingDataSet = Filter.useFilter(testingDataSet, stringToWordVectorFilter);
        ArffSaver saveTestFile = new ArffSaver();
        saveTestFile.setInstances(filteredTestingDataSet);
        saveTestFile.setFile(new File("resources/label/test.arff"));
        saveTestFile.writeBatch();
        System.out.println("Done ..  test file persisted.");
    }

    public AttributeSelection createFilter(int numAtts) {

        AttributeSelection filter = new AttributeSelection();
        try {

            Ranker search = new Ranker();
            search.setNumToSelect(numAtts);

            GainRatioAttributeEval eval = new GainRatioAttributeEval();
            //InfoGainAttributeEval eval = new InfoGainAttributeEval();
            //ReliefFAttributeEval eval = new ReliefFAttributeEval();
            filter.setEvaluator(eval);
            filter.setSearch(search);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filter;
    }


    public void attributeScoring(Instances tr, String filePath) {
        try {
            FileUtils fileUtils = new FileUtils();
            File file = new File(filePath);
            double e1, e2, e3;
            AttributeEvaluator as = new InfoGainAttributeEval();
            AttributeEvaluator as2 = new GainRatioAttributeEval();
            AttributeEvaluator as3 = new ChiSquaredAttributeEval();
            fileUtils.writeStringToFile(file, "INDEX,NAME,IG,IGR,CHI");
            for (int i = 0; i < tr.numAttributes(); i++) {
                e1 = as.evaluateAttribute(i);
                e2 = as2.evaluateAttribute(i);
                e3 = as3.evaluateAttribute(i);

                fileUtils.writeStringToFile(file, i + "," + tr.attribute(i).name() + "," + e1 + "," + e2 + "," + e3);
                System.out.println(i + "," + tr.attribute(i).toString() + e1 + "," + e2 + "," + e3);
            }
        } catch (Exception e) {
            System.out.println("Exception thrown in attributeScoring = " + e.toString());
        }
    }


    private Classifier getClassifier(Instances filteredTrainingDataSet) throws Exception {
        System.out.println("Wait ..  processing attribute selection");
        ASEvaluation attributeFilter = new ChiSquaredAttributeEval();
        System.out.println("Done ..  Selected attributes and created training data set.");
        Classifier decisionTree = new J48();
        return buildClassifier(filteredTrainingDataSet, attributeFilter, decisionTree);
    }

    private GreedyStepwise isSearchBackWord() {
        GreedyStepwise attributeSearchModelForRanking = new GreedyStepwise();
        attributeSearchModelForRanking.setSearchBackwards(true);
        return attributeSearchModelForRanking;
    }


    private Classifier buildClassifier(Instances trainingData, ASEvaluation asEvaluation, Classifier classifier) throws Exception {
        System.out.println("Wait ..  Building classifier");

        AttributeSelectedClassifier attributeSelectedClassifier = new AttributeSelectedClassifier();
        attributeSelectedClassifier.setClassifier(classifier);
        attributeSelectedClassifier.setEvaluator(asEvaluation);
        attributeSelectedClassifier.setSearch(isSearchBackWord());

        classifier.buildClassifier(trainingData);
        System.out.println("\n\nClassifier model:\n\n" + classifier);
        System.out.println("Done ..  Trained classifier");
        return classifier;
    }

    private void evaluateTestingDataSetGivenTrainingDataSet(Filter filter, Instances dataFiltered, Classifier decisionTreeJ48) throws Exception {
        Instances testRawData = loadDirDataSetToWekaFormat(LABEL_TEST_DATA_SET_DIR_PATH);
        Instances testingData = filteredData(filter, testRawData);
        Debug.saveToFile("resources/testing.arff", testingData);
        evaluateTestingDataOnClassifier(dataFiltered, decisionTreeJ48, testingData);
    }

    private void evaluateCrossFoldModel(Instances dataFiltered, Classifier decisionTreeJ48) throws Exception {
        Evaluation evaluation = new Evaluation(dataFiltered);
        evaluation.crossValidateModel(decisionTreeJ48, dataFiltered, 4, new Debug.Random(1));
        System.out.println(evaluation.toSummaryString());
        System.out.println("Done ..  Evaluation");
    }

    private StringToWordVector filterStringToWordVectorType(Instances dataRaw) throws Exception {
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(dataRaw);
        filter.setStemmer(new SnowballStemmer());
        filter.setStopwords(new File("resources/stopword/engstopwords.txt"));
        return filter;
    }

    private Instances loadDirDataSetToWekaFormat(String dataSetDirPath) throws IOException {
        TextDirectoryLoader testLoader = new TextDirectoryLoader();
        testLoader.setDirectory(new File(dataSetDirPath));
        return testLoader.getDataSet();
    }

    private Instances filteredData(Filter filter, Instances testRawData) throws Exception {
        System.out.println("Wait .. Converting test data set to word vector.");

        Instances testingData = Filter.useFilter(testRawData, filter);

        System.out.println("Done .. Converted test data set to word ̄Avector.");
        return testingData;
    }

    private void evaluateTestingDataOnClassifier(Instances dataFiltered, Classifier decisionTreeJ48, Instances testingData) throws Exception {
        System.out.println("Wait .. Evaluating testing data on trained classifier.");

        Evaluation testEvaluation = new Evaluation(dataFiltered);
        testEvaluation.evaluateModel(decisionTreeJ48, testingData);
        System.out.println(testEvaluation.toSummaryString("\nResults\n======\n", false));

        System.out.println("Exit .. All processing done.");
    }

    public String predict(String testDataDirPath) {
        return null;
    }

    public Instances trainedData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/label/train.arff"));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        return arff.getData();
    }

    public Instances testingData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/label/test.arff"));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        return arff.getData();
    }


    public Instances trainedDataProduct() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/product/train.arff"));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        return arff.getData();
    }

    public Instances testingDataProduct() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/product/test.arff"));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        return arff.getData();
    }


    public Instances gainRatioAttributeSelection(Instances trainingData, int numAtts) {

        AttributeSelection filter = new AttributeSelection();
        try {
            Ranker search = new Ranker();
            search.setNumToSelect(numAtts);
            GainRatioAttributeEval eval = new GainRatioAttributeEval();
            filter.setEvaluator(eval);
            filter.setSearch(search);
            filter.setInputFormat(trainingData);
            return Filter.useFilter(trainingData, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trainingData;
    }

    public Instances infoGainAttributeSelection(Instances trainingData, int numAtts) {


        AttributeSelection filter = new AttributeSelection();
        try {

            Ranker search = new Ranker();
            search.setNumToSelect(numAtts);

            InfoGainAttributeEval eval = new InfoGainAttributeEval();
            filter.setEvaluator(eval);
            filter.setSearch(search);
            filter.setInputFormat(trainingData);
            return Filter.useFilter(trainingData, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trainingData;
    }

    public Instances chisquareAttributeSelection(Instances trainingData, int numAtts) {

        AttributeSelection filter = new AttributeSelection();
        try {

            Ranker search = new Ranker();
            search.setNumToSelect(numAtts);

            ChiSquaredAttributeEval eval = new ChiSquaredAttributeEval();
            filter.setEvaluator(eval);
            filter.setSearch(search);
            filter.setInputFormat(trainingData);
            return Filter.useFilter(trainingData, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainingData;
    }

    public Instances labelTrainedData() throws Exception {
        NumericToNominal convert= new NumericToNominal();
        String[] options= new String[2];
        options[0]="-R";
        options[1]="first-last";  //range of variables to make numeric

        convert.setOptions(options);
        convert.setInputFormat(trainedData());
        return Filter.useFilter(trainedData(), convert);
    }

    public Instances labelTestData() throws Exception {
        NumericToNominal convert= new NumericToNominal();
        String[] options= new String[2];
        options[0]="-R";
        options[1]="first-last";  //range of variables to make numeric

        convert.setOptions(options);
        convert.setInputFormat(trainedData());
        return Filter.useFilter(trainedData(), convert);
    }

    public Classifier buildClassifier(Instances instance, Classifier classifier) throws Exception {
        classifier.buildClassifier(instance);
        System.out.println("\n\nClassifier model:\n\n" + classifier);
        System.out.println("Done ..  Trained classifier");
        return classifier;
    }

    public Evaluation testingClassifierForLabelData(Classifier classifierModel, Instances instance) throws Exception {
        instance.setClassIndex(0);
        Evaluation evaluation = new Evaluation(instance);
        Instances testingData = labelTestData();
        Instances attributeSelectedByChiSquare = chisquareAttributeSelection(testingData, 1000);

        evaluation.evaluateModel(classifierModel, attributeSelectedByChiSquare);
        return evaluation;
    }

    public Instances productTrainedData() throws Exception {
        NumericToNominal convert= new NumericToNominal();
        String[] options= new String[2];
        options[0]="-R";
        options[1]="first-last";  //range of variables to make numeric

        convert.setOptions(options);
        convert.setInputFormat(trainedDataProduct());
        return Filter.useFilter(trainedDataProduct(), convert);
    }

    public Instances productTestedData() throws Exception {
        NumericToNominal convert= new NumericToNominal();
        String[] options= new String[2];
        options[0]="-R";
        options[1]="first-last";  //range of variables to make numeric

        convert.setOptions(options);
        convert.setInputFormat(trainedDataProduct());
        return Filter.useFilter(testingDataProduct(), convert);
    }

    public Evaluation testingClassifierForProductData(Classifier classifierModel, Instances instance) throws Exception {
        instance.setClassIndex(0);
        Evaluation testEvaluation = new Evaluation(instance);
        Instances testingDataSet = productTestedData();
        Instances attributeSelectedByChiSquare = chisquareAttributeSelection(testingDataSet, 1000);
        testEvaluation.evaluateModel(classifierModel, attributeSelectedByChiSquare);
        return testEvaluation;
    }
}
