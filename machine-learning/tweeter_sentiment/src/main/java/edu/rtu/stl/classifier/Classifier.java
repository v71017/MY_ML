package edu.rtu.stl.classifier;

import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.DomainObject;
import edu.rtu.stl.domain.Sentiment;

public interface Classifier<T> {

    Result classify(Document line);

    Classifier<T> trainClassifier(T data);

    default Result findMax(double[] score, Document document) {
        Result max = new Result(Sentiment.values()[0], score[0], document);
        for (int i = 1; i < Sentiment.values().length; i++) {
            if (max.score < score[i]) {
                max = new Result(Sentiment.values()[i], score[i], document);
            }
        }
        return max;
    }

    class Result extends DomainObject {
        public final Sentiment sentiment;
        public final double score;
        public final Document document;

        public Result(Sentiment sentiment, double score, Document document) {
            this.sentiment = sentiment;
            this.score = score;
            this.document = document;
        }
    }
}
