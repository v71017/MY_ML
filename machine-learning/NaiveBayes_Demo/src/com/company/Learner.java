package com.company;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Pratama Agung on 6/15/2017.
 */
public class Learner {
    private Instances trainData;
    private FilteredClassifier classifier;
    private StringToWordVector filter;

    /**
     * Method to get the classifier which has been trained in this class
     * @return classifier
     */
    public FilteredClassifier getClassifier(){
        return classifier;
    }

    /**
     * method to load the data training
     * @param fileName path to training file relative to project location
     */
    public void loadDataset(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            trainData = new Instances(reader);
            System.out.println("Loaded dataset: " + fileName);
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to evaluate the loaded data training
     */
    public void evaluate() {
        try {
            trainData.setClassIndex(0);
            filter = new StringToWordVector();

            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayesMultinomial());
            Evaluation eval = new Evaluation(trainData);
            eval.crossValidateModel(classifier, trainData, 2, new Random(1));
            System.out.println("Evaluating dataset done");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method for generate classifier from loaded data training
     */
    public void learn() {
        try {
            trainData.setClassIndex(0);
            filter = new StringToWordVector();
            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayesMultinomial());
            classifier.buildClassifier(trainData);

            System.out.println("Training dataset done");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
