/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spamfilter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class SpamFilterMainClass {
    static boolean small = false; 
    static String numberOfDataSet = "1"; 
    static final String RawDataPath = "Data/Raw/"; 
    static final String arffTrainFile = "Data/Weka/Data/Train.arff"; 
    static final String arffTestFile = "Data/Weka/Data/Test.arff"; 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        //Build the data model
        try {
            DataModel.initiateModel(RawDataPath);
            RawToARFF.createARFF();
            TrainAndTestData.trainData(); 
            
            WordStatistics wordStat = new WordStatistics(RawDataPath +"HamTest" + numberOfDataSet, RawDataPath +"SpamTest" + numberOfDataSet); 
            wordStat.getStatistics(); 
            
        } catch (IOException ex) {
            System.out.println(ex.toString()); 
        }
    }
    
}
