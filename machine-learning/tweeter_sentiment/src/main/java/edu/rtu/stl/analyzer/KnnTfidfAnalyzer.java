package edu.rtu.stl.analyzer;

import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.KnnTfidfDataSet;
import edu.rtu.stl.domain.TFIDFDocument;
import edu.rtu.stl.parser.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Mordavolt.
 */
public class KnnTfidfAnalyzer implements Analyzer<KnnTfidfDataSet> {

    private static final Logger LOG = LoggerFactory.getLogger(KnnTfidfAnalyzer.class);

    private final Tokenizer tokenizer;

    public KnnTfidfAnalyzer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public KnnTfidfDataSet analyze(List<Document> documents) {
        KnnTfidfDataSet dataSet = new KnnTfidfDataSet();
        LOG.debug("Starting analyzing.");
        for (Document document : documents) {
            TFIDFDocument workDocument = dataSet.addDocument(document);
            for (String key : tokenizer.tokenize(document.text)) {
                dataSet.addTerm(document.sentiment, key);
                workDocument.addTerm(key);
            }
        }
        LOG.debug("Starting TF-IDF value calculation.");
        dataSet.calculateTFIDFValues();
        LOG.debug("TF-IDF value calculation done.");
        return dataSet;
    }
}
