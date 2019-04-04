package edu.rtu.stl.runner;

import static java.lang.System.in;

import java.util.Scanner;

import edu.rtu.stl.classifier.Classifier;
import edu.rtu.stl.conf.ClassificationConfiguration;
import edu.rtu.stl.domain.Document;

public class InteractiveRunner extends Runner {

    public InteractiveRunner(ClassificationConfiguration conf, Loader loader) {
        super(conf, loader);
    }

    @Override
    public void run() {
        Classifier classifier = conf.trainClassifier(loader.getFullData());

        Scanner scanner = new Scanner(in);
        System.out.println("Enter some text to classify");
        System.out.println(">");
        String line = scanner.nextLine();
        while (!"\\q".equals(line)) {
            Classifier.Result classificationResult = classifier.classify(new Document(null, line));
            System.out.println("Classified as: " + classificationResult.sentiment);

            System.out.println(">");
            line = scanner.nextLine();
        }

    }

}
