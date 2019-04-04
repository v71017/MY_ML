package prediction.models;

import org.apache.commons.io.FileUtils;
import training.PredictionModel;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dewadkar on 11/08/16.
 */
public class FordCarLabelPredictionModel extends PredictionModel {

    public static final String RESOURCES_EVALUATION_LABEL_CLASSIFIER_EVALUATION_TXT = "resources/evaluation/label/classifierEvaluation.txt";
    public static final String RESOURCES_LABEL_SELECTED_ATTRIBUTES_LABEL_DATA_TXT = "resources/label/selectedAttributesLabelData.txt";
    public static final int NUMBER_OF_ATTRIBUTES = 1000;

    public static void main(String[] args) {
        PredictionModel predictionModel = new FordCarLabelPredictionModel();
        try {
            Instances trainingData = predictionModel.labelTrainedData();
            Instances attributeSelectedByChiSquare = predictionModel.chisquareAttributeSelection(trainingData, NUMBER_OF_ATTRIBUTES);
            Instances attributeSelectedByInfoGain = predictionModel.infoGainAttributeSelection(trainingData, NUMBER_OF_ATTRIBUTES);
            Instances attributeSelectedByGainRatio = predictionModel.gainRatioAttributeSelection(trainingData, NUMBER_OF_ATTRIBUTES);
//            File file = new File(RESOURCES_LABEL_SELECTED_ATTRIBUTES_LABEL_DATA_TXT);
//            for (int i = 0; i < NUMBER_OF_ATTRIBUTES; i++) {
//                String data = attributeSelectedByChiSquare.attribute(i).name() + " "+attributeSelectedByInfoGain.attribute(i).name()+ " "+attributeSelectedByGainRatio.attribute(i).name()+"\n";
//                System.out.print(data);
//                FileUtils.writeStringToFile(file,data,true);
//            }

            List<Classifier> classifiers = new LinkedList<Classifier>();
            classifiers.add(new NaiveBayes());
            classifiers.add(new J48());
            classifiers.add(new LibSVM());

            List<Instances> instances = new LinkedList<Instances>();
            instances.add(attributeSelectedByChiSquare);
            instances.add(attributeSelectedByGainRatio);
            instances.add(attributeSelectedByInfoGain);

            File evaluationFile = new File(RESOURCES_EVALUATION_LABEL_CLASSIFIER_EVALUATION_TXT);
            for (Classifier classifier: classifiers) {
                FileUtils.writeStringToFile(evaluationFile,"Classifier "+classifier.getClass().getName(),true);
                for (Instances instance : instances) {
                    FileUtils.writeStringToFile(evaluationFile,"Instance "+instance.getClass().getName());
                    Classifier classifierModel =  predictionModel.buildClassifier(instance, classifier);
                    Evaluation evaluation = predictionModel.testingClassifierForLabelData(classifierModel,instance);
                    FileUtils.writeStringToFile(evaluationFile,evaluation.toSummaryString(),true);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
