package main.java;

import java.io.*;
import java.util.*;

/**
 * Created by dewadkar on 8/12/2016.
 */
public class DataSet {


    private List<String[]> termsDocsArray = new ArrayList();
    private List<String> allTerms = new ArrayList();
    private List<double[]> tfidfDocsVector = new ArrayList();


    public DataSet(String documentDir) throws IOException {
        this.parseFiles(documentDir);
        this.tfIdfCalculator(termsDocsArray, allTerms);
    }

    private void parseFiles(String filePath) throws IOException {
        File[] allFiles = new File(filePath).listFiles();
        BufferedReader in = null;
        for (File f : allFiles) {
            in = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
            for (String term : tokenizedTerms) {
                if (!allTerms.contains(term)) {
                    allTerms.add(term);
                }
            }
            termsDocsArray.add(tokenizedTerms);
        }
    }

    public String[] parseFile(final String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            sb.append(s);
        }
        String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
       return tokenizedTerms;
    }


    private void tfIdfCalculator(final List<String[]> termsDocsArray, final List<String> allTerms) {
        double tf;
        double idf;
        double tfidf;
        for (String[] docTermsArray : termsDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
                tf = new TfIdf().tfCalculator(docTermsArray, terms);
                idf = new TfIdf().idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.add(tfidfvectors);  //storing document vectors;
        }
    }

    public double[] tfIdfForDocument(final String documentFilePath) throws IOException {
        double tf;
        double idf;
        double tfidf;
        double[] tfidfvectors = new double[allTerms.size()];
        int count = 0;
        for (String terms : allTerms) {
            tf = new TfIdf().tfCalculator(parseFile(documentFilePath), terms);
            idf = new TfIdf().idfCalculator(termsDocsArray, terms);
            tfidf = tf * idf;
            tfidfvectors[count] = tfidf;
            count++;
        }
        return tfidfvectors;
    }

    private void getCosineSimilarity(List tfidfDocsVector, List tfidfDocsVectorTest) {
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            for (int j = 0; j < tfidfDocsVectorTest.size(); j++) {
                System.out.println("between " + i + " and " + j + "  =  "
                        + new CosineSimilarity().cosineSimilarity(tfidfDocsVector.get(i), tfidfDocsVector.get(j))
                );
            }
        }
    }


    public Map<Integer, Double> cosineSimilarityForDocument(String documentPath) throws IOException {
        double[] singleDocTFIDFVector= tfIdfForDocument(documentPath);
        Map<Integer, Double> similarityMap = new HashMap<Integer, Double>();
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
                similarityMap.put(i,new CosineSimilarity().cosineSimilarity(tfidfDocsVector.get(i), singleDocTFIDFVector));
            }
            return similarityMap;
    }

    public void topNSimilar(Map<Integer, Double> map,int topNSimilarDoc) {
        MyComparator comparator = new MyComparator(map);
        Map<Integer, Double> newMap = new TreeMap<Integer, Double>(comparator);
        newMap.putAll(map);
        int numberOfDocAdded =0;
        for (Map.Entry<Integer, Double> entry : newMap.entrySet()) {
            System.out.println("Document Number = " + entry.getKey() + ", Similarity = " + entry.getValue());
            numberOfDocAdded++;
            if(numberOfDocAdded == topNSimilarDoc) break;
        }
    }


    private static class  MyComparator implements Comparator {

        Map map;

        public MyComparator(Map map) {
            this.map = map;
        }

        public int compare(Object o1, Object o2) {

            return ((Double) map.get(o2)).compareTo((Double) map.get(o1));


        }
    }

}


