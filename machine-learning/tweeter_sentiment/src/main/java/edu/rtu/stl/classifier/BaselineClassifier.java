package edu.rtu.stl.classifier;

import edu.rtu.stl.domain.BaselineDataSet;
import edu.rtu.stl.domain.Document;

public class BaselineClassifier implements Classifier<BaselineDataSet> {

    private BaselineDataSet dataSet;

    @Override
    public Result classify(Document document) {
        return new Result(dataSet.getRandomSentiment(), 1, document);
    }

    @Override
    public Classifier<BaselineDataSet> trainClassifier(BaselineDataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }
}
