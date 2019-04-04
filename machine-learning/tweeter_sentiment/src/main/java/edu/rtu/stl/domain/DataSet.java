package edu.rtu.stl.domain;

import java.util.HashMap;
import java.util.Map;

public class DataSet {

    private SentimentFrequency total = new SentimentFrequency();
    private final SentimentFrequency[] sentiments = new SentimentFrequency[Sentiment.values().length];

    {
        for (int i = 0; i < Sentiment.values().length; i++) {
            sentiments[i] = new SentimentFrequency();
        }
    }

    public Map<String, Integer> getFrequencies(Sentiment sentiment) {
        return sentiments[sentiment.ordinal()].frequencies;
    }

    public Map<String, Integer> getFrequencies() {
        return total.frequencies;
    }

    public void addTerm(Sentiment sentiment, String key) {
        addTerm(total, key);
        addTerm(sentiments[sentiment.ordinal()], key);
    }

    public int getDocumentCount(Sentiment sentiment) {
        return sentiments[sentiment.ordinal()].documentCount;
    }

    public int getTotalTerms(Sentiment sentiment) {
        return sentiments[sentiment.ordinal()].termCount;
    }

    public int getTotalTerms() {
        return total.termCount;
    }

    public int getDocumentCount() {
        return total.documentCount;
    }

    public int distinctTermCount() {
        return total.frequencies.size();
    }

    public void incrementDocumentCount(Sentiment sentiment) {
        total.documentCount++;
        sentiments[sentiment.ordinal()].documentCount++;
    }

    private void addTerm(SentimentFrequency sentimentFrequency, String key) {
        sentimentFrequency.frequencies.put(key, sentimentFrequency.frequencies.getOrDefault(key, 0) + 1);
        sentimentFrequency.termCount++;
    }

    private static class SentimentFrequency {
        private final Map<String, Integer> frequencies = new HashMap<>();
        private int termCount = 0;
        private int documentCount = 0;
    }
}
