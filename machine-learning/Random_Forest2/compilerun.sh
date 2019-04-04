#!/bin/bash

/usr/java1.6/bin/javac -d bin/ -cp lib/weka.jar src/dataclassifiers/BaggingClassifier.java src/dataclassifiers/DecisionTreeClassifier.java src/dataclassifiers/NaiveBayesClassifier.java src/dataclassifiers/RandomForestClassifier.java src/dataclassifiers/StackingClassifier.java src/core/Evaluator.java src/core/Predictor.java


#run
/usr/java1.6/bin/java -Xmx128m -cp bin:lib/weka.jar core.Predictor
exit
