package edu.rtu.stl.analyzer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.Sentiment;
import edu.rtu.stl.domain.WekaDataSet;

public class WekaAnalyzer implements Analyzer<WekaDataSet> {

    private static final Logger LOG = LoggerFactory.getLogger(WekaAnalyzer.class);

    @Override
    public WekaDataSet analyze(List<Document> documents) {

        WekaDataSet dataSet = new WekaDataSet(10);

        for (Sentiment sentiment : Sentiment.values()) {
            dataSet.addCategory(sentiment);
        }

        dataSet.buildAttributes();

        for (Document document : documents) {
            dataSet.addData(document.text, document.sentiment);
        }

        LOG.info("Created data set");

        return dataSet;
    }

}

