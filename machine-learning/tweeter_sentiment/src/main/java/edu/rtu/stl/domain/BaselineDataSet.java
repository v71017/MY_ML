package edu.rtu.stl.domain;

import java.util.Random;

public class BaselineDataSet extends DataSet {

    private Random random = new Random();

    public Sentiment getRandomSentiment() {

        int index = random.nextInt(getDocumentCount());
        int total = 0;

        for (int i = 0; i < Sentiment.values().length; i++) {
            total += getDocumentCount(Sentiment.values()[i]);
            if (total >= index) {
                return Sentiment.values()[i];
            }
        }

        return Sentiment.values()[Sentiment.values().length - 1];
    }
}
