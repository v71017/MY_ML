/*
 * returns statistics of the most common words in ham and spam emails. 
 * Should only be used if you want this information. 
 * This file is not necessary for the training and classification
 *
 * The datamodel parameters must have been initialized before running the functions in this class. 
 */
package spamfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author alex
 */
public class WordStatistics {
    private final String HamPath; 
    private final String SpamPath; 
    public int topSpamWords = 200; 
    public int topHamWords = 200; 
    public  HashMap<String, Integer> WordCountHam = new HashMap<String,Integer>(); 
    public  HashMap<String, Integer> WordCountSpam = new HashMap<String,Integer>();
    
    
    public WordStatistics(String hamPath, String spamPath){
        this.HamPath = hamPath; 
        this.SpamPath = spamPath; 
    }

    /**
     * Returns a String with the statistics over the most common words for each class 
     * @return 
     */
    public String getStatistics() throws IOException{
        initiateWordCount();
        getHamStatistics(); 
        getSpamStatistics();
        return printStatistics();  
    }
    
    /**
     * Fills up the word count hash map with a word count
     * @return
     * @throws IOException 
     */
    private void getHamStatistics() throws IOException{
        System.out.println("Getting ham statistics"); 
        File hamFile = new File(HamPath);
        // Find all words from the ham emails
        for(File file: hamFile.listFiles()){
            try { 
                BufferedReader b = new BufferedReader(new FileReader(file));
                Scanner scanner = null; 
                String line = b.readLine(); 
                while(line != null){
                    scanner = new Scanner(line);
                    while(scanner.hasNext()){
                        String next = scanner.next(); 
                        if(WordCountHam.get(next) != null){
                            WordCountHam.put(next, WordCountHam.get(next)+1); 
                        }
                    }
                    line = b.readLine(); 
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            }
        }
    }
    
    
    private void getSpamStatistics() throws IOException{
        System.out.println("Getting spam statistics");
        File hamFile = new File(SpamPath);
        // Find all words from the ham emails
        for(File file: hamFile.listFiles()){
            try { 
                BufferedReader b = new BufferedReader(new FileReader(file));
                Scanner scanner = null; 
                String line = b.readLine(); 
                while(line != null){
                    scanner = new Scanner(line);
                    while(scanner.hasNext()){
                        String next = scanner.next(); 
                        if(WordCountSpam.get(next) != null){
                            WordCountSpam.put(next, WordCountSpam.get(next)+1); 
                        }
                    }
                    line = b.readLine(); 
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex.toString());
            }
        }
    }
    
    /**
     * Initiates the keys for the word count hash map
     */
    private void initiateWordCount(){
        for(String key: DataModel.bloomFilter.keySet()){
            WordCountHam.put(key, 0);
            WordCountSpam.put(key, 0);
        }
    }

    private String printStatistics() {
        TreeMap<String, Integer> sortedHam = SortByValue(WordCountHam);
        TreeMap<String, Integer> sortedSpam = SortByValue(WordCountSpam);
        
        System.out.println(); 
        System.out.println("Ham Words: "); 
        int i = 0; 
        for(String key: sortedHam.keySet()){
            if(i > topHamWords) break; 
            System.out.println(i + ". " + key); 
            i++; 
        }
        
        System.out.println(); 
        System.out.println("Spam Words: "); 
        i = 0; 
        for(String key: sortedSpam.keySet()){
            if(i > topSpamWords) break; 
            System.out.println(i + ". " + key); 
            i++; 
        }
        return ""; 
    }
    
    
    public TreeMap<String, Integer> SortByValue(HashMap<String, Integer> map) {
        ValueComparator vc =  new ValueComparator(map);
        TreeMap<String,Integer> sortedMap = new TreeMap<String,Integer>(vc);
        sortedMap.putAll(map);
        return sortedMap;
    }
    
    class ValueComparator implements Comparator<String> {
 
        Map<String, Integer> map;

        public ValueComparator(Map<String, Integer> base) {
            this.map = base;
        }

        public int compare(String a, String b) {
            if (map.get(a) >= map.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys 
        }
    }
    
}
