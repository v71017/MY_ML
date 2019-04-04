package edu.rtu.stl.classifier;

import static java.lang.Math.log;

import edu.rtu.stl.domain.BayesDataSet;
import edu.rtu.stl.domain.Sentiment;
import edu.rtu.stl.parser.Tokenizer;

public class BernulliNaiveBayesClassifier extends NaiveBayesClassifier {

    public BernulliNaiveBayesClassifier(Tokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    double incrementScore(Sentiment sentiment, String key) {
        if (dataSet.getFrequencies(sentiment).containsKey(key)) {
            return log(dataSet.bernulliTermProbability(sentiment, key));
        }

        return log(1 - dataSet.bernulliTermProbability(sentiment, key));
    }
}
