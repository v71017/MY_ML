package edu.rtu.stl;

import static edu.rtu.stl.domain.RunnerType.INTERACTIVE;
import static edu.rtu.stl.domain.RunnerType.STATISTICAL;

import edu.rtu.stl.conf.AnalyzerConfiguration;
import edu.rtu.stl.conf.ClassificationConfiguration;
import edu.rtu.stl.domain.ClassificationType;
import edu.rtu.stl.domain.LoaderType;
import edu.rtu.stl.domain.RunnerType;
import edu.rtu.stl.runner.CrossValidationRunner;
import edu.rtu.stl.runner.InteractiveRunner;
import edu.rtu.stl.runner.Loader;
import edu.rtu.stl.util.WithCollections;
import edu.rtu.stl.util.WithDefaultProperties;

public class App implements WithDefaultProperties, WithCollections {

    private final AnalyzerConfiguration analyzerConfiguration = new AnalyzerConfiguration();

    public static void main(String[] args) throws Exception {
        new App().start();
    }

    public void start() {
        RunnerType runnerType = RunnerType.valueOf(getProperty("runner.type"));
        LoaderType loaderType = LoaderType.valueOf(getProperty("loader.type"));
        ClassificationType classType = ClassificationType.valueOf(getProperty("classification.type"));

        Loader loader = find(analyzerConfiguration.loaders, x -> x.getType() == loaderType);
        ClassificationConfiguration classConf = find(analyzerConfiguration.classificationConfigurations, x -> x.type() == classType);

        if (runnerType == INTERACTIVE) {
            new InteractiveRunner(classConf, loader).run();
        }

        if (runnerType == STATISTICAL) {
            new CrossValidationRunner(classConf, loader).run();
        }
    }


}
