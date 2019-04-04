/*
 * Uses the weka library(in this case a naive bayes classifier) to train a model 
 * and test it with the testdata. 
 */

package spamfilter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random; 

import weka.core.Instances; 
import weka.classifiers.trees.J48; 
import weka.classifiers.bayes.NaiveBayesSimple; 
import weka.classifiers.Evaluation; 
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM; 
import weka.core.Instance;

/**
 *
 * @author alex
 */
public class TrainAndTestData {
    
    public static void trainData() throws Exception{
        BufferedReader reader = null; 
        Instances trainData = null;
        Instances testData = null;
        
        try { 
            reader = new BufferedReader(new FileReader(SpamFilterMainClass.arffTrainFile));
            trainData = new Instances(reader);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString()); 
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                System.err.println(ex.toString()); 
            }
        } //Read in testdata also
        try { 
            reader = new BufferedReader(new FileReader(SpamFilterMainClass.arffTestFile));
            testData = new Instances(reader);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString()); 
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                System.err.println(ex.toString()); 
            }
        }
        
        trainData.setClassIndex(trainData.numAttributes() -1);
        testData.setClassIndex(testData.numAttributes() -1); 
        
        
        //Naive Bayes Classifier
        NaiveBayesMultinomial naive = new NaiveBayesMultinomial(); 
        naive.buildClassifier(trainData);
        
        //Testing on testdata
        Evaluation eval = new Evaluation(trainData); 
        eval.evaluateModel(naive, testData); 
        System.out.println("summary " + eval.toSummaryString());
        double[][] confMatrix = eval.confusionMatrix(); 
        
        for(int i = 0; i < testData.numInstances(); i++){
            double realClass = testData.instance(i).classValue(); 
            if(naive.classifyInstance(testData.instance(i)) != realClass){
                System.out.println("Instance number " + i);
                System.out.println("RealClass " + realClass);
                System.out.println("Classified as: " + naive.classifyInstance(testData.instance(i)));
            }
        }
    
    }
    
}
