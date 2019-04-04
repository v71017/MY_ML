package edu.rtu.stl.conf;

import java.util.List;

import edu.rtu.stl.analyzer.Analyzer;
import edu.rtu.stl.classifier.Classifier;
import edu.rtu.stl.domain.ClassificationType;
import edu.rtu.stl.domain.Document;

public class ClassificationConfiguration<T> {

    private final ClassificationType type;
    private final Analyzer<T> analyzer;
    private final Classifier<T> classifier;

    public ClassificationConfiguration(ClassificationType type, Analyzer<T> analyzer, Classifier<T> classifier) {
        this.type = type;
        this.analyzer = analyzer;
        this.classifier = classifier;
    }

    public ClassificationType type() {
        return type;
    }

    public Classifier<T> trainClassifier(List<Document> documents) {
        return classifier.trainClassifier(analyzer.analyze(documents));
    }
}
