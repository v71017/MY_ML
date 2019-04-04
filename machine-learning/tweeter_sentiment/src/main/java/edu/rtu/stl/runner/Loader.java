package edu.rtu.stl.runner;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.LoaderType;
import edu.rtu.stl.util.WithFileReading;

public abstract class Loader implements WithFileReading {
    private final LoaderType type;

    protected Loader(LoaderType type) {
        this.type = type;
    }

    public abstract List<Document> getFullData();

    public List<Document> getData() {
        List<Document> documents = getFullData();
        Random random = new Random(getLongProperty("data.randomize.seed", System.currentTimeMillis()));
        Collections.shuffle(documents, random);
        return documents.subList(0, Math.min(getLongProperty("data.size.limit", Integer.MAX_VALUE).intValue(), documents.size()));
    }


    public LoaderType getType() {
        return type;
    }
}
