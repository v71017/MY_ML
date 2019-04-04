package edu.rtu.stl.analyzer;

import java.util.List;

import edu.rtu.stl.domain.BayesDataSet;
import edu.rtu.stl.domain.Document;
import edu.rtu.stl.parser.Tokenizer;

public class BayesAnalyzer implements Analyzer<BayesDataSet> {

    private final Tokenizer tokenizer;

    public BayesAnalyzer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public BayesDataSet analyze(List<Document> documents) {
        BayesDataSet dataSet = new BayesDataSet();
        for (Document document : documents) {
            dataSet.incrementDocumentCount(document.sentiment);
            for (String key : tokenizer.tokenize(document.text)) {
                dataSet.addTerm(document.sentiment, key);
            }
        }
        return dataSet;
    }

}
