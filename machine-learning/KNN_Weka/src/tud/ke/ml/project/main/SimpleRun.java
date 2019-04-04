package tud.ke.ml.project.main;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import weka.classifiers.lazy.keNN;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class SimpleRun {

	private static Instances data;
	private static RemovePercentage filterTrain,filterTest;
	private static keNN classifier = new keNN();

	public static void main(String[] args) {
		try {
			setUp();
			test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setUp() throws Exception {
		//Laden der Daten
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File("data/contact-lenses.arff"));
		data = loader.getDataSet();
		//Sezten des Klassenattributes
		data.setClassIndex(data.numAttributes()-1);

		//Instanziieren des Klassifizieres
		classifier = new keNN();

		//Erstellen der Filter f�r Test und Trainingsdaten
		double percentageSplit=60;
		filterTrain = new RemovePercentage();
		filterTrain.setPercentage(percentageSplit);
		filterTrain.setInvertSelection(true); //nimmt die ersten percentageSplit% als Trainingsdaten 
		filterTest = new RemovePercentage();
		filterTest.setPercentage(percentageSplit);	
		filterTrain.setInputFormat(data);
		filterTest.setInputFormat(data);
//		System.out.println(data.numInstances());
//		System.out.println(Filter.useFilter(data, filterTrain).numInstances());
//		System.out.println(Filter.useFilter(data, filterTest).numInstances());
	}

	public static void test() throws Exception {		
		//Erstellen des Modells
		classifier.buildClassifier(Filter.useFilter(data, filterTrain));
		//Klassifizieren der Trainingsdaten
		Instances testData = Filter.useFilter(data, filterTest);
		for(Instance instance : testData) {
			double result = classifier.classifyInstance(instance);
			String klasse = ""+result;
			if(instance.classAttribute().isNominal()) klasse = instance.classAttribute().value((int) result);
			System.out.println("The Instance \""+instance.toString()+"\" was classified as a class \""+klasse+"\"");
		}
	}

}
