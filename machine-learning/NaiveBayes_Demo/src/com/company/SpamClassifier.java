package com.company;

import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by Pratama Agung on 6/15/2017.
 */
public class SpamClassifier {
    String message;
    Instances instances;

    /**
     * Constructor for class SpamClassifier
     * @param message string which will be classified as spam or not_spam
     */
    public SpamClassifier(String message){
        this.message = message;
    }

    /**
     * Method to make instance of the message
     */
    public void makeInstance() {

        FastVector fvNominalVal = new FastVector(2);
        fvNominalVal.addElement("spam");
        fvNominalVal.addElement("not_spam");
        Attribute attribute1 = new Attribute("class", fvNominalVal);
        Attribute attribute2 = new Attribute("text",(FastVector) null);

        FastVector fvWekaAttributes = new FastVector(2);
        fvWekaAttributes.addElement(attribute1);
        fvWekaAttributes.addElement(attribute2);
        instances = new Instances("Test relation", fvWekaAttributes, 1);
        instances.setClassIndex(0);

        Instance instance = new Instance(2);
        instance.setValue(attribute2, message);

        instances.add(instance);
        System.out.println("Instance created");
    }

    /**
     * Method to classify the message
     * @param classifier result of learning from data set
     */
    public int classify(Classifier classifier) {
        try {
            double[] pred = classifier.distributionForInstance(instances.instance(0));
            System.out.println("Message classified" + Arrays.toString(pred));
            int result;
            if (pred[0] > pred [1]){
                if (pred[0] > 0.75){
                    result = 0;
                } else {
                    result = 1;
                }
            } else {
                if (pred[1] > 0.75) {
                    result = 4;
                } else {
                    result = 3;
                }
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
