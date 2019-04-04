package prediction.models;

import org.apache.commons.io.FileUtils;
import training.PredictionModel;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.mi.MISVM;
import weka.classifiers.trees.J48;
import weka.core.Debug;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dewadkar on 11/08/16.
 */
public class FordCarPredictionModel extends PredictionModel {


    public static final String RESOURCES_EVALUATION_PRODUCT_CLASSIFIER_EVALUATION_TXT = "resources/evaluation/product/classifierEvaluation.txt";
    public static final String RESOURCES_PRODUCT_SELECTED_ATTRIBUTES_PRODUCT_DATA_TXT = "resources/product/selectedAttributesProductData.txt";
    public static final int NUMBER_OF_ATTRIBUTES = 1000;

    public static void main(String[] args) {
        PredictionModel predictionModel = new FordCarPredictionModel();
        try {
            Instances trainingData = predictionModel.productTrainedData();
            Instances attributeSelectedByChiSquare = predictionModel.chisquareAttributeSelection(trainingData, NUMBER_OF_ATTRIBUTES);
            Instances attributeSelectedByInfoGain = predictionModel.infoGainAttributeSelection(trainingData, NUMBER_OF_ATTRIBUTES);
            Instances attributeSelectedByGainRatio = predictionModel.gainRatioAttributeSelection(trainingData, NUMBER_OF_ATTRIBUTES);
            List<Instances> instances = new LinkedList<Instances>();
            instances.add(attributeSelectedByChiSquare);
            instances.add(attributeSelectedByGainRatio);
            instances.add(attributeSelectedByInfoGain);

            File file = new File(RESOURCES_PRODUCT_SELECTED_ATTRIBUTES_PRODUCT_DATA_TXT);
            for (int attributeIndex = 0; attributeIndex < NUMBER_OF_ATTRIBUTES; attributeIndex++) {
                String data = attributeSelectedByChiSquare.attribute(attributeIndex).name() + " "+attributeSelectedByInfoGain.attribute(attributeIndex).name()+ " "+attributeSelectedByGainRatio.attribute(attributeIndex).name()+"\n";
                FileUtils.writeStringToFile(file,data,true);
            }

            List<Classifier> classifiers = new LinkedList<Classifier>();
            classifiers.add(new NaiveBayes());
            classifiers.add(new J48());
            classifiers.add(new LibSVM());



            File evaluationFile = new File(RESOURCES_EVALUATION_PRODUCT_CLASSIFIER_EVALUATION_TXT);
            evaluateByTrainingData(predictionModel, classifiers, instances, evaluationFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void evaluateByTrainingData(PredictionModel predictionModel, List<Classifier> classifiers, List<Instances> instances, File evaluationFile) throws Exception {
        for (Classifier classifier: classifiers) {
            FileUtils.writeStringToFile(evaluationFile,"Classifier "+classifier.getClass().getName());
            evaluateClassifier(predictionModel, instances, evaluationFile, classifier);
        }
    }

    private static void evaluateClassifier(PredictionModel predictionModel, List<Instances> instances, File evaluationFile, Classifier classifier) throws Exception {
        for (Instances instance : instances) {
            FileUtils.writeStringToFile(evaluationFile,"Instance "+instance.getClass().getName());

            Classifier classifierModel =  predictionModel.buildClassifier(instance, classifier);
            Evaluation evaluation = predictionModel.testingClassifierForProductData(classifierModel,instance);
            FileUtils.writeStringToFile(evaluationFile,evaluation.toSummaryString(),true);
        }
    }
}
