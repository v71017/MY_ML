package edu.rtu.stl.classifier;

import static java.lang.Math.log;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.rtu.stl.domain.BayesDataSet;
import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.Sentiment;
import edu.rtu.stl.parser.Tokenizer;

public abstract class NaiveBayesClassifier implements Classifier<BayesDataSet> {

    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesClassifier.class);

    private final Tokenizer tokenizer;
    BayesDataSet dataSet;

    public NaiveBayesClassifier(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public Result classify(Document document) {
        LOG.debug("Classifying document: " + document.text);

        Iterable<String> tokens = tokenizer.tokenize(document.text);
        double[] score = new double[Sentiment.values().length];

        for (int i = 0; i < Sentiment.values().length; i++) {
            Sentiment sentiment = Sentiment.values()[i];
            score[i] = log(dataSet.sentimentProbability(sentiment));
            for (String token : tokens) {
                score[i] += incrementScore(sentiment, token);
            }
        }

        return findMax(score, document);
    }

    @Override
    public NaiveBayesClassifier trainClassifier(BayesDataSet data) {
        this.dataSet = data;
        return this;
    }

    abstract double incrementScore(Sentiment sentiment, String key);
}
