package edu.rtu.stl.runner;

import edu.rtu.stl.conf.ClassificationConfiguration;

public abstract class Runner {

    public final ClassificationConfiguration<?> conf;
    public final Loader loader;

    public Runner(ClassificationConfiguration<?> conf, Loader loader) {
        this.conf = conf;
        this.loader = loader;
    }

    abstract void run();
}
