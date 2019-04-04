package edu.rtu.stl.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class BayesDataSet extends DataSet {

    private final Map<String, Double>[] multinomialTermProbabilities = new Map[Sentiment.values().length];
    private final Map<String, Double>[] bernulliTermProbabilities = new Map[Sentiment.values().length];

    public BayesDataSet() {

        for (int i = 0; i < Sentiment.values().length; i++) {
            multinomialTermProbabilities[i] = new HashMap<>();
            bernulliTermProbabilities[i] = new HashMap<>();
        }
    }

    public double multinomialTermProbability(Sentiment sentiment, String key) {
        return getOrComputeProbability(multinomialTermProbabilities, sentiment, key, this::multinomialProbability);
    }

    public double bernulliTermProbability(Sentiment sentiment, String key) {
        return getOrComputeProbability(bernulliTermProbabilities, sentiment, key, this::bernulliProbability);
    }

    public double sentimentProbability(Sentiment sentiment) {
        return getDocumentCount(sentiment) / (double) (getDocumentCount());
    }

    private double getOrComputeProbability(Map<String, Double>[] cache, Sentiment sentiment, String key,
            BiFunction<Sentiment, Integer, Double> probabilityFunction) {
        if (cache[sentiment.ordinal()].containsKey(key)) {
            return cache[sentiment.ordinal()].get(key);
        }

        if (!getFrequencies(sentiment).containsKey(key)) {
            return probabilityFunction.apply(sentiment, 0);
        }

        int documentCountContainingTerm = getFrequencies(sentiment).get(key);

        double result = probabilityFunction.apply(sentiment, documentCountContainingTerm);
        cache[sentiment.ordinal()].put(key, result);
        return result;
    }

    private double multinomialProbability(Sentiment sentiment, int termFrequencyInSentiment) {
        int totalTermCountInSentiment = getTotalTerms(sentiment);
        int distinctTermCount = distinctTermCount();

        return termFrequencyInSentiment + 1 / (double) (totalTermCountInSentiment + distinctTermCount);
    }

    private double bernulliProbability(Sentiment sentiment, int documentCountContainingTerm) {
        int documentCount = getDocumentCount();

        return documentCountContainingTerm + 1 / (double) (documentCount + 2);
    }

}
