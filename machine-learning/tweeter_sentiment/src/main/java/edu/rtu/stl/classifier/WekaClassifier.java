package edu.rtu.stl.classifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.WekaDataSet;
import edu.rtu.stl.util.WithException;
import edu.rtu.stl.util.WithWeka;
import weka.core.Instance;
import weka.core.Instances;

public class WekaClassifier implements Classifier<WekaDataSet>, WithException, WithWeka {

    private static final Logger LOG = LoggerFactory.getLogger(WekaClassifier.class);

    private WekaDataSet dataSet;
    private final weka.classifiers.Classifier wekaClassifier;

    public WekaClassifier(weka.classifiers.Classifier wekaClassifier) {
        this.wekaClassifier = wekaClassifier;
    }

    @Override
    public Result classify(Document line) {
        LOG.debug("Classifying document " + line.text);
        Instances testSet = dataSet.getTrainingData().stringFreeStructure();
        Instance testInstance = makeInstance(line.text.toLowerCase(), testSet);

        return exceptional(() -> findMax(wekaClassifier.distributionForInstance(testInstance), line));
    }

    @Override
    public WekaClassifier trainClassifier(WekaDataSet data) {
        this.dataSet = data;
        exceptional(() -> wekaClassifier.buildClassifier(data.getTrainingData()));
        LOG.info("Trained classifier");
        return this;
    }

}
