/*
 * Will transform the raw data to arff files. 
 *
 * The Datamodel parameters must be initialized before doing this. 
 */

package spamfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The data must be divided into folders "spam" and "ham"
 * @author alex
 */
public class RawToARFF {
    
    /**
     * Writes to the file data.arff in the folder data/weka/
     */
    public static  void createARFF() throws IOException{
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("Data/Weka/Data/Train.arff", "UTF-8"); 
            writeHeader(writer);
            writeBody(writer, "/HamTrain"+ SpamFilterMainClass.numberOfDataSet, "/SpamTrain"+ SpamFilterMainClass.numberOfDataSet);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString()); 
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString()); 
        } finally {
            writer.close();
        }
        
        //same for testdata
        try {
            writer = new PrintWriter("Data/Weka/Data/Test.arff", "UTF-8"); 
            writeHeader(writer);
            writeBody(writer, "/HamTest" + SpamFilterMainClass.numberOfDataSet, "/SpamTest"+ SpamFilterMainClass.numberOfDataSet);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString()); 
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString()); 
        } finally {
            writer.close();
        }
    }
    
    
    private static void writeHeader(PrintWriter writer){
        writer.println("@RELATION emails");
        for(int i = 0; i < DataModel.numberOfWords; i++){
            writer.println("@ATTRIBUTE " + i + " NUMERIC"); 
        }
        writer.println("@ATTRIBUTE class {spam,ham}"); 
        writer.println("@DATA"); 
    }
    
    private static void writeBody(PrintWriter writer, String hamPath, String spamPath) throws IOException{
        File hamFile;
        File spamFile;  
        

        hamFile = new File(SpamFilterMainClass.RawDataPath + hamPath);
        spamFile = new File(SpamFilterMainClass.RawDataPath + spamPath);
        
        Scanner scanner; 
        int[] attributes = new int[DataModel.numberOfWords]; 
        
        // Find all words from the ham emails
        for(File file: hamFile.listFiles()){
            Arrays.fill(attributes, 0);
            try { 
                scanner = new Scanner(file); 
                while(scanner.hasNext()){
                    String token = scanner.next(); 
                    if(DataModel.wordToIndex.containsKey(token)){
                        attributes[DataModel.wordToIndex.get(token)]++; 
                    }
                }
                for(int i = 0; i < DataModel.numberOfWords; i++){
                    writer.print(Integer.toString(attributes[i]) + ",");
                }
                writer.print("ham \r\n"); 
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            }
        }
        
        
        // Find all words from the ham emails
        for(File file: spamFile.listFiles()){
            Arrays.fill(attributes, 0);
            try { 
                scanner = new Scanner(file); 
                while(scanner.hasNext()){
                    String token = scanner.next(); 
                    if(DataModel.wordToIndex.containsKey(token)){
                        attributes[DataModel.wordToIndex.get(token)]++; 
                    }
                }
                for(int i = 0; i < DataModel.numberOfWords; i++){
                    writer.print(Integer.toString(attributes[i]) + ",");
                }
                writer.print("spam \r\n");
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            }
        }
    }
}
