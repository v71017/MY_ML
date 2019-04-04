package edu.rtu.stl.util;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public interface WithWeka {

    default Instance makeInstance(String text, Instances data) {
        Instance instance = new DenseInstance(2);
        Attribute attribute = data.attribute("text");
        instance.setValue(attribute, attribute.addStringValue(text));
        instance.setDataset(data);
        return instance;
    }
}
