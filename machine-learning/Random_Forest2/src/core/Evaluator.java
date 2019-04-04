package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Evaluator {
	
	Evaluation evaluator = null;
	double[] predictions = null;
	
	// Constructor
	public Evaluator(Instances trainingData) {

		try {
			// Create evaluator
			evaluator = new Evaluation(trainingData);
			
		} catch (Exception e) {
			System.out.println("Unable to create evaluator.");
			System.exit(1);
		}	
	}
	
	public void doEvaluation(Classifier classifier, Instances data) {
		try {
			predictions = evaluator.evaluateModel(classifier, data);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public String confusionMatrixToString() {
		double[][] matrix = evaluator.confusionMatrix();
		int day = 0;
		String confusionMatrix = "Confusion Matrix\n======================\n" +
				"Mon\tTue\tWed\tThu\tFri\tSat\tSun\n";
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				confusionMatrix = confusionMatrix + (int) matrix[i][j] + "\t";
			}
			confusionMatrix = confusionMatrix + getDayString(day) + "\n";
			day++;
		}
		
		return confusionMatrix;
	}
	
	private String getDayString(int value) {
		String day = "";
		
		switch (value) {
		case 0:
			day = "Mon";
			break;
		case 1:
			day = "Tue";
			break;
		case 2:
			day = "Wed";
			break;
		case 3:
			day = "Thu";
			break;
		case 4:
			day = "Fri";
			break;
		case 5:
			day = "Sat";
			break;
		case 6:
			day = "Sun";
			break;
		default:
			break;
		}
		
		return day;
	}
	
	public String getEvaluationStats() {
		return evaluator.toSummaryString();
	}
	
	public Evaluation getEvaluator() {
		return evaluator;
	}
	
	public double[] getPredictions(Classifier classifier, Instances data) {
		return predictions;
	}
	
	public void printPredictions() {
		int count = 0;
		
		for (int i = 0; i < predictions.length; i++) {
			if (count < 15) {
				System.out.print(predictions[i] + ", ");
				count++;
			}
			else {
				System.out.println(predictions[i] + ", ");
				count = 0;
			}
		}
	}
	
	public void writePredictionsToFile(String outputLocation, Instances data, String additionalText) {

		System.out.print("Writing predictions to file... ");
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			
			File file = new File(outputLocation);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			if (Predictor.VERBOSE) {
				// Save evaluation stats
				fw.write(getEvaluationStats() + "\n");
				
				// Save confusion matrix
				fw.write(confusionMatrixToString() + "\n");
				
				// Save any additional data
				fw.write(additionalText + "\n\n");
			}
			
			
			// write to file
			for (int i = 0; i < data.numInstances(); i++) {
				String outputString = "";
				
				for (int j = 0; j < data.instance(i).numAttributes(); j++) {
					
					if (data.instance(i).attribute(j).name().equals("F1") ||
							data.instance(i).attribute(j).name().equals("F2") ||
							data.instance(i).attribute(j).name().equals("F3")) {
						
						// set year, month and date
						outputString = outputString + (int)data.instance(i).value(j) + ",";
					}
					else if (j == 0) {
						// set city
						outputString = outputString + data.instance(i).stringValue(j) + ",";
					}
					else if (j == (data.instance(i).numAttributes() - 1)) {
						// set prediction
						outputString = outputString + getDayString((int) predictions[i]) + "\n";
					}
					else {
						//set everything else
						if (data.instance(i).isMissing(j)) {
							outputString = outputString + "?,";
						}
						else {
							
							if (data.instance(i).attribute(j).isNominal()){
								outputString = outputString + data.instance(i).stringValue(j) + ",";
							}
							else {
								outputString = outputString + data.instance(i).value(j) + ",";
							}
							
						}
						
					}
				}
				
				bw.write(outputString);
			}
			
			
			bw.close();
 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}	
		}
		
		System.out.print("Done!\n");
	}
}
