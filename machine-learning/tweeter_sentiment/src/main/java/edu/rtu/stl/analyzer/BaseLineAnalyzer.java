package edu.rtu.stl.analyzer;

import edu.rtu.stl.domain.BaselineDataSet;
import edu.rtu.stl.domain.Document;

import javax.print.Doc;
import java.util.List;

/**
 * Created by a.drozdovs on 09/05/2016.
 */
public class BaseLineAnalyzer implements Analyzer<BaselineDataSet> {
    @Override
    public BaselineDataSet analyze(List<Document> documents) {
        BaselineDataSet dataSet = new BaselineDataSet();
        documents.stream().forEach(document -> dataSet.incrementDocumentCount(document.sentiment));
        return dataSet;
    }
}
