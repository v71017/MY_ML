import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instance;
import weka.core.Instances;

public class LearningProcess {
	public static int m;
	public static void main(String[] args) throws Exception {
//		if (args.length != 3) {
//			 System.out.println("usage: dt-learn <train-set-file> <test-set-file> m");
//			 System.exit(-1);
//		}
		// read the training file 
		BufferedReader reader = new BufferedReader(new FileReader("/Users/vpati/Intuit_project/ml/dt2/Machine-Learning-DecisionTree/diabetes_train.arff"));
		Instances train_set = new Instances(reader);
		/*
		// randomly get rid of some instance for part 2
		ArrayList<Integer> add = new ArrayList<Integer>();
		int n;
		int counter = 0;
		while (counter < 20) {
			Random rand = new Random();
			n = rand.nextInt(100);
			if (add.contains(n) == false) {
				add.add(n);
				counter ++;
			}
		}
		BufferedReader reader2 = new BufferedReader(new FileReader("diabetes_train2.arff"));
		Instances train_set2 = new Instances(reader2);
		for (int i=0; i<add.size(); i++) {
			train_set2.add(train_set.get(add.get(i)));
		}*/
		
		// if class attribute is not defined
		if (train_set.classIndex() == -1) {
			train_set.setClassIndex(train_set.numAttributes() - 1);
		}
		// get the m value and check it
		m = 20;//Integer.parseInt(args[2]);
		if (m > train_set.size()) {
			System.out.println("Error: m must not be bigger than the size of the train set");
			 System.exit(-1);
		}
		// generate the decision tree and print
		DecisionTree decisionTree = new DecisionTree(train_set, m);
		decisionTree.print();
		System.out.println("<Predictions for the Test Set Instances>");
		// read the testing file
		reader = new BufferedReader(new FileReader("/Users/vpati/Intuit_project/ml/dt2/Machine-Learning-DecisionTree/diabetes_test.arff"));
		Instances test_set = new Instances(reader);
		reader.close();
		// if class attribute is not defined
		if (test_set.classIndex() == -1) {
			test_set.setClassIndex(test_set.numAttributes() - 1);
		}
		int correct = 0;
		for (int j = 0; j < test_set.numInstances(); j++) {
			int instance_index = j + 1;
			Instance test_instance = test_set.instance(j);
			// trace through the decision tree to get prediction
			String predicted = decisionTree.traverse(test_instance);
			// get the actual value from class attribute
			String actual = test_instance.stringValue(test_set.classAttribute());
			System.out.println(instance_index + ": Actual: " + actual + " Predicted: " + predicted);
			if (predicted.equals(actual)) {
				correct ++;
			}
		}
		System.out.println("Number of correctly classified: " + correct +" Total number of test instances: " + test_set.size());
	}
}