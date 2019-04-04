package edu.rtu.stl.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mordavolt.
 */
public class TFIDFDocument {

    public final Map<String, Double> termTFIDFMap = new HashMap<>();
    public final Map<String, Integer> termFrequencyMap = new HashMap<>();
    private Document document;


    public TFIDFDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public Sentiment getSentiment() {
        return document.sentiment;
    }

    public String getText() {
        return document.text;
    }

    public void addTerm(String term) {
        termFrequencyMap.put(term, termFrequencyMap.getOrDefault(term, 1));
    }

    public void addTFIDFValue(String term, Double tfidf) {
        termTFIDFMap.put(term, tfidf);
    }

    public Set<String> getTerms() {
        return termFrequencyMap.keySet();
    }

    public Integer getMaxTermFrequency() {
        return Collections.max(termFrequencyMap.values());
    }

    public Integer getTermFrequency(String term) {
        return termFrequencyMap.getOrDefault(term, 0);
    }

    public Double getTermTFIDF(String term) {
        return termTFIDFMap.getOrDefault(term, 0.0);
    }
}
