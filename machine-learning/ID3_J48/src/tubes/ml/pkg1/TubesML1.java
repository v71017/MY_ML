/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubes.ml.pkg1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.classifiers.trees.J48;
import weka.classifiers.Classifier;
import weka.classifiers.trees.Id3;
import weka.classifiers.Evaluation;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.supervised.instance.Resample;

/**
 * @author Alriana
 */
public class TubesML1{

    public void akses() throws Exception{
        Discretize filter;
        int fold = 10;
        int fold3 = 3;
        int trainNum,testNum;
        PrintWriter file = new PrintWriter( "model.txt" );

        /***dataset 1***/
        file.println("***DATASET 1***");
        fileReader tets = new fileReader("./src/data/iris.arff");
        try {
            tets.read();
        } catch (IOException ex) {
            Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
        }
        Instances data = tets.getData();
        filter= new Discretize();
        try {
            filter.setInputFormat(data);
        } catch (Exception ex) {
            Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*ID3*/
        Instances discreteData;
        discreteData = Filter.useFilter(data, filter);
        trainNum = discreteData.numInstances()*3/4;
        testNum = discreteData.numInstances()/4;
        
        for (int i = 0; i <fold;i++){
            try {
                
                Instances train = discreteData.trainCV(fold, i);
                Instances test = discreteData.testCV(fold, i);

            
                Id3 iTiga = new Id3();
                Evaluation validation = new Evaluation(train);
                try {
                    iTiga.buildClassifier(train);
                    System.out.println(iTiga.toString());
                    file.println(iTiga.toString());
                } catch (Exception ex) {
                    Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
                }
                validation.evaluateModel(iTiga, test);
                System.out.println(validation.toSummaryString());
                file.println("Validation "+ (i+1));
                file.println(validation.toSummaryString());
            } catch (Exception ex) {
                Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /*J48*/
        trainNum = data.numInstances()*3/4;
        testNum = data.numInstances()/4;
        J48 jKT = new J48();
        for (int i = 0; i <fold;i++){
            Instances train = data.trainCV(fold, i);
            Instances test = data.testCV(fold, i);         
            try {
                Evaluation validation = new Evaluation(train);
                try {
                    jKT.buildClassifier(data);
                } catch (Exception ex) {
                    Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
                }
                validation.evaluateModel(jKT, test);
                System.out.println(validation.toSummaryString());
                file.println("Validation "+ (i+1));
                file.println(validation.toSummaryString());
           // System.out.println(jKT.toString());
            } catch (Exception ex) {
                Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        
        /*dataset 2*/
        file.println("***DATASET 2***");
        tets.setFilepath("./src/data/weather.arff");
        try {
            tets.read();
        } catch (IOException ex) {
            Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
        }
        data = new Instances(tets.getData());
        
        /*ID3*/
        discreteData = Filter.useFilter(data, filter);
        trainNum = discreteData.numInstances()*3/4;
        testNum = discreteData.numInstances()/4;
        
        for (int i = 0; i <fold3;i++){
            try {                
                Instances train = discreteData.trainCV(trainNum, i);
                Instances test = discreteData.testCV(testNum, i);
            
                Id3 iTiga = new Id3();
                Evaluation validation = new Evaluation(train);
                try {
                    iTiga.buildClassifier(train);
                    System.out.println(iTiga.toString());
                    //file.println(iTiga.toString());
                } catch (Exception ex) {
                    Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
                }
                validation.evaluateModel(iTiga, test);
                System.out.println(validation.toSummaryString());
                file.println("Validation "+ (i+1));
                file.println(validation.toSummaryString());
            } catch (Exception ex) {
                Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(testNum);
        file.println("Test Number");
        file.println(testNum);
        
        /*J48*/
        trainNum = data.numInstances()*3/4;
        testNum = data.numInstances()/4;
        
        for (int i = 0; i <fold;i++){
            Instances train = data.trainCV(fold, i);
            Instances test = data.testCV(fold, i);  
            try {
                Evaluation validation = new Evaluation(train);
                try {
                    jKT.buildClassifier(data);
                } catch (Exception ex) {
                    Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
                }
                validation.evaluateModel(jKT, test);
                System.out.println(validation.toSummaryString());
                file.println(validation.toSummaryString());
                System.out.println(jKT.toString());
                file.println(jKT.toString());
            } catch (Exception ex) {
                Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        
        /*dataset 3*/
        file.println("***DATASET 3***");
        tets.setFilepath("./src/data/weather.nominal.arff");
        try {
            tets.read();
        } catch (IOException ex) {
            Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
        }
        data = new Instances(tets.getData());
        
        /*ID3*/
        discreteData = Filter.useFilter(data, filter);
        trainNum = discreteData.numInstances()*3/4;
        testNum = discreteData.numInstances()/4;
        
        for (int i = 0; i <fold3;i++){
            try {                
                Instances train = discreteData.trainCV(fold, i);
                Instances test = discreteData.testCV(fold, i);
            
                Id3 iTiga = new Id3();
                Evaluation validation = new Evaluation(train);
                try {
                    iTiga.buildClassifier(train);
                    System.out.println(iTiga.toString());
                    file.println(iTiga.toString());
                } catch (Exception ex) {
                    Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
                }
                validation.evaluateModel(iTiga, test);
                System.out.println(validation.toSummaryString());
                file.println(validation.toSummaryString());
            } catch (Exception ex) {
                Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(testNum);
        file.println("Test Number");
        file.println(testNum);
        
        /*J48*/
        trainNum = data.numInstances()*3/4;
        testNum = data.numInstances()/4;
        
        for (int i = 0; i <fold;i++){
            Instances train = data.trainCV(fold, i);
            Instances test = data.testCV(fold, i);
            try {
                Evaluation validation = new Evaluation(train);
                try {
                    jKT.buildClassifier(data);
                } catch (Exception ex) {
                    Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
                }
                validation.evaluateModel(jKT, test);
                System.out.println(validation.toSummaryString());
                file.println(validation.toSummaryString());
                System.out.println(jKT.toString());
                file.println(jKT.toString());
            } catch (Exception ex) {
                Logger.getLogger(TubesML1.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        
        /*RESULTT*/
        System.out.println(jKT.toString());
        file.println("RESULT");
        file.println( jKT.toString() );
        file.close();
    }
    public static void main(String[] args) throws Exception {
        TubesML1 testCase1 = new TubesML1();
        testCase1.akses();
        
    }
    
    
}
