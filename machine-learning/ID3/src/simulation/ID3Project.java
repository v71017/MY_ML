package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import tree.DecisionTree;
import weka.core.Instances;

import application.Classifier;
import application.ManagerDataSet;
import application.Id3;
import application.ManagerDisplay;


public class ID3Project {

	
	public static void main(String[] args) {
	
		
		
			
		try {
			
			//FIXME INSERT HERE THE NEW DATA SET
			BufferedReader b = new BufferedReader(new FileReader("data/autos.arff"));
			Instances dataSet;
			dataSet = new Instances(b);			
			dataSet.setClassIndex(dataSet.numAttributes()-1);
			
			dataSet=ManagerDataSet.discretize(dataSet);
			Instances[]split=ManagerDataSet.split(dataSet);		
			
			DecisionTree id3_tree=Id3.decisionTreeLearner(split[0],null,null);	
			
			ManagerDisplay.displayTree(id3_tree);
			
			double error_trainingSet=Classifier.classifyTestingSet(split[0], id3_tree);
		    double error_testingSet=Classifier.classifyTestingSet(split[1], id3_tree);
			System.out.println("Training Set error: "+Math.round( error_trainingSet* Math.pow( 10, 2 ) )/Math.pow( 10, 2 )+"%");
			System.out.println("Testing Set error: "+Math.round( error_testingSet*100* Math.pow( 10, 2 ) )/Math.pow( 10, 2 )+"%");
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 
		
	
	}

}
