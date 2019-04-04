/*
 * Defines the feature extractor model. 
 * 
 * The model is determined by iterating over all files and counting the number
 * of relevant words and creating a bloomfilter for these words. 
 *
 * The bloom filter and other parameters are then used globally in this project. 
 */

package spamfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class DataModel {
    static String[] words; 
    static int numberOfWords; 
    static final int wordLengthLimit = 2; 
    static HashMap<String, Boolean> bloomFilter = new HashMap<String, Boolean>(); 
    static HashMap<String, Integer> wordToIndex = new HashMap<String, Integer>(); 
    static HashMap<String, Boolean> stopList = new HashMap<String, Boolean>(); 
    static String stopListPath = "Data/Stopwords/StopList_MySQL";
    
    public static void initiateModel(String rawDataPath) throws IOException{
        File hamFile;
        File spamFile;  
        

        hamFile = new File(rawDataPath + "HamTrain" + SpamFilterMainClass.numberOfDataSet);
        spamFile = new File(rawDataPath + "SpamTrain" + SpamFilterMainClass.numberOfDataSet);

        //Build stoplist
        File stopListFile = new File(stopListPath); 
        BufferedReader stopListReader = new BufferedReader(new FileReader(stopListFile)); 
        String stopLine = stopListReader.readLine(); 
        while(stopLine != null){
            stopList.put(stopLine, Boolean.TRUE);
            stopLine = stopListReader.readLine(); 
        }
        
        // Find all words from the ham emails
        for(File file: hamFile.listFiles()){
            try { 
                BufferedReader b = new BufferedReader(new FileReader(file));
                String line = b.readLine(); 
                while(line != null){
                    buildBloomFilter(line);
                    line = b.readLine(); 
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            }
        }
        
        // Find all words from the ham emails
        for(File file: spamFile.listFiles()){
            try { 
                BufferedReader b = new BufferedReader(new FileReader(file));
                String line = b.readLine(); 
                while(line != null){
                    buildBloomFilter(line);
                    line = b.readLine(); 
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            }
        }
        
        //Read off the bloom filter to get the words
        numberOfWords = bloomFilter.size(); 
        words = new String[numberOfWords]; 
        int index = 0; 
        for(String word: bloomFilter.keySet()){
            words[index] = word; 
            wordToIndex.put(word, index); 
            index++;
        }
    }
    
    /**
     * Builds the bloom filter by receiving text input
     * Ignores words in the stoplist
     * 
     * @param fileText 
     */
    private static void buildBloomFilter(String fileText){
        Scanner textScanner = new Scanner(fileText); 
        String current = textScanner.next(); 
        current = current.toLowerCase(); 
        while(current != null  && stopList.get(current) == null ){ //If not in Stoplist
            if(current.length() >= wordLengthLimit){
                if(bloomFilter.get(current) == null){
                    try{
                       Integer.parseInt(current);    //Avoid numbers 
                    }
                    catch(NumberFormatException e){
                        bloomFilter.put(current, Boolean.TRUE); 
                    }
                }
            }
            if(textScanner.hasNext() == true)
                current = textScanner.next(); 
            else
                current = null; 
        }
    }
}
