package edu.rtu.stl.domain;

import edu.rtu.stl.classifier.Classifier;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Mordavolt.
 */
public class KnnTfidfDataSet extends DataSet {
    private final List<TFIDFDocument> frequencyDocumentList = new ArrayList<>();
    private final Map<String, Integer> termDocumentMentionCount = new HashMap<>();

    public TFIDFDocument addDocument(Document document) {
        TFIDFDocument workDocument = new TFIDFDocument(document);
        frequencyDocumentList.add(workDocument);
        incrementDocumentCount(document.sentiment);
        return workDocument;
    }


    public void calculateTFIDFValues() {
        frequencyDocumentList.forEach(this::calculateTFIDFValue);
    }

    public void calculateTFIDFValue(TFIDFDocument document) {
        for (Map.Entry<String, Integer> entry : document.termFrequencyMap.entrySet()) {
            String term = entry.getKey();
            Integer termFrequency = entry.getValue();
            document.addTFIDFValue(term, (0.5+0.5*(termFrequency/document.getMaxTermFrequency()))
                    * log(getDocumentCount() / getDocumentWithTermCount(term)));
        }
    }

    private Integer getDocumentWithTermCount(String term) {
        Integer documentMentions = termDocumentMentionCount.get(term);
        if (documentMentions == null) {
            documentMentions = findDocumentsWithTerm(term).size();
            termDocumentMentionCount.put(term, documentMentions);
        }
        return documentMentions!= 0 ? documentMentions : 1;
    }

    private List<TFIDFDocument> findDocumentsWithTerm(String term) {
        return frequencyDocumentList.stream().filter(
                document -> document.getTerms().contains(term)
        ).collect(Collectors.toList());
    }

    public List<TFIDFDocument> findKNearestNeighbours(TFIDFDocument document, Integer K) {
        Map<Double, TFIDFDocument> neighbourDistanceMap = new HashMap<>();
        for(TFIDFDocument doc : frequencyDocumentList) {
            neighbourDistanceMap.put(findDistanceBetweenDocuments(document, doc), doc);
        }

        return neighbourDistanceMap.keySet().stream().sorted().limit(K).map(neighbourDistanceMap::get).collect(Collectors.toList());
    }

    public Double findDistanceBetweenDocuments(TFIDFDocument doc1, TFIDFDocument doc2) {
        Set<String> termUnion = new HashSet<>(doc1.getTerms());
        termUnion.addAll(doc2.getTerms());

        return sqrt(termUnion.stream().map(term -> pow(doc1.getTermTFIDF(term) - doc2.getTermTFIDF(term), 2)).reduce((a, b) -> a + b).orElse(Double.MAX_VALUE));
    }

    public Classifier.Result calculateKNNSentiment(TFIDFDocument workDocument, Integer K) {
        List<TFIDFDocument> kNearestNeighbours = findKNearestNeighbours(workDocument, K);
        Map<Sentiment, Long> kNearestNeighbourTypes = kNearestNeighbours.stream().map(TFIDFDocument::getSentiment)
                .collect(groupingBy(identity(), counting()));

        Map.Entry<Sentiment, Long> maxEntry = null;
        for (Map.Entry<Sentiment, Long> entry : kNearestNeighbourTypes.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        return new Classifier.Result(maxEntry.getKey(), maxEntry.getValue()/kNearestNeighbours.size(), workDocument.getDocument());
    }
}
