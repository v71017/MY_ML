package edu.rtu.stl.domain;

import java.util.ArrayList;
import java.util.List;

import edu.rtu.stl.util.WithWeka;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class WekaDataSet implements WithWeka {

    public WekaDataSet(int startSize) {
        this.attributes = new ArrayList<>(2);
        this.attributes.add(new Attribute("text", (List<String>) null));
        this.classValues = new ArrayList<>(startSize);
        this.setup = false;
    }

    private Instances trainingData;
    private ArrayList<String> classValues;
    private ArrayList<Attribute> attributes;
    private boolean setup;

    public WekaDataSet addCategory(Sentiment sentiment) {
        classValues.add(sentiment.name().toLowerCase());
        return this;
    }

    public WekaDataSet buildAttributes() {
        attributes.add(new Attribute("classValueAttributeShouldBeUniqueSoDon'tChangeThis", classValues));
        trainingData = new Instances("MessageClassificationProblem", attributes, 100);
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        setup = true;
        return this;
    }

    public WekaDataSet addData(String message, Sentiment sentiment) throws IllegalStateException {
        if (!setup) {
            throw new IllegalStateException("Must use setup first");
        }
        Instance instance = makeInstance(message.toLowerCase(), trainingData);
        instance.setClassValue(sentiment.name().toLowerCase());
        trainingData.add(instance);
        return this;
    }

    public Instances getTrainingData() {
        return trainingData;
    }
}
