package edu.rtu.stl.runner;

import edu.rtu.stl.classifier.Classifier;
import edu.rtu.stl.conf.ClassificationConfiguration;
import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.Report;
import edu.rtu.stl.domain.Sentiment;
import edu.rtu.stl.report.HoldoutPartitioner;
import edu.rtu.stl.report.HoldoutPartitioner.HoldoutPartition;
import edu.rtu.stl.report.PrecisionRecallReportGenerator;
import edu.rtu.stl.util.WithDefaultProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class CrossValidationRunner extends Runner implements WithDefaultProperties {

    private static final Logger LOG = LoggerFactory.getLogger(CrossValidationRunner.class);

    public CrossValidationRunner(ClassificationConfiguration conf, Loader loader) {
        super(conf, loader);
    }

    @Override
    public void run() {
        Map<Sentiment, ArrayList<Report>> crossValidationReports = new ConcurrentHashMap<>();
        for (Sentiment sentiment : Sentiment.values()) {
            crossValidationReports.put(sentiment, new ArrayList<>());
        }
        LOG.debug("Starting load.");
        List<Document> documents = Collections.unmodifiableList(loader.getData());
        LOG.debug("Load done.");
        PrecisionRecallReportGenerator reportGenerator = new PrecisionRecallReportGenerator();
        HoldoutPartitioner holdoutPartitioner = new HoldoutPartitioner();

        LongStream.range(0, getLongProperty("runner.cross-validation.iteration.count", 10)).forEach(i -> {
            LOG.debug("Starting {} iteration.", i);
            HoldoutPartition data = holdoutPartitioner.partition(documents);
            LOG.debug("Starting training.");
            Classifier classifier = conf.trainClassifier(data.trainingSet);
            LOG.debug("Training done.");

            List<Classifier.Result> classificationResults = data.testingSet.stream().map(classifier::classify).collect(Collectors.toList());

            for (Sentiment sentiment : Sentiment.values()) {
                Report r = reportGenerator.generateFor(sentiment, classificationResults);
                crossValidationReports.compute(sentiment, (x, y) -> {
                    y.add(r);
                    return y;
                });
            }
        });

        for (Entry<Sentiment, ArrayList<Report>> entry : crossValidationReports.entrySet()) {
            LOG.info("Result for sentiment {}: {}", entry.getKey(), entry.getValue().stream().reduce(
                    Report::combine).get());
        }

    }
}

