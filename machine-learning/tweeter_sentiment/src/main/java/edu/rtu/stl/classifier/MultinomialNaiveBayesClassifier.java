package edu.rtu.stl.classifier;

import static java.lang.Math.log;

import edu.rtu.stl.domain.BayesDataSet;
import edu.rtu.stl.domain.Sentiment;
import edu.rtu.stl.parser.Tokenizer;

public class MultinomialNaiveBayesClassifier extends NaiveBayesClassifier {

    public MultinomialNaiveBayesClassifier(Tokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    double incrementScore(Sentiment sentiment, String key) {
        return log(dataSet.multinomialTermProbability(sentiment, key));
    }

}
