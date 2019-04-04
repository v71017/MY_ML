import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ambar
 * 
 * Below is the main class to create the Decision Tree using 
 * Information Gain Heuristic and Variance Impurity Heuristic
 */
public class ProgramDT {
	private static final String SPLIT_SYMBOL = ",";
	public static String[] featureName = null;
	public static boolean ENTROPY = true;

	/**
	 * @param args : Program requires arguments as <training-set> <validation-set> <test-set> <to-print> The argument to-print accepts value {yes,no}
	 */
	public static void main(String[] args) {
		// Below lines for testing purpose.
		
		 /*String trainingFile = "D:\\Courses\\ML-580L\\HW1\\dataset2\\training_set.csv";
		 String validationFile = "D:\\Courses\\ML-580L\\HW1\\dataset2\\validation_set.csv"; 
		 String testFile = "D:\\Courses\\ML-580L\\HW1\\dataset2\\test_set.csv";
		 String toPrint = "no";
		 int L=25;
		 int K=15;
		 */
//		if(args.length != 6) {
//			System.out.println("Invalid Arguments: Please refer the README.txt for instructions");
//			return;
//		}
		Integer L= 25;//Integer.parseInt(args[0]);
		Integer K= 15; //Integer.parseInt(args[1]);
		String trainingFile ="/Users/vpati/Intuit_project/ml/dt/dt1/Decision-Tree/dataset1/training_set.csv" ;//args[2];
		String validationFile = "/Users/vpati/Intuit_project/ml/dt/dt1/Decision-Tree/dataset1/validation_set.csv";//args[3];
		String testFile = "/Users/vpati/Intuit_project/ml/dt/dt1/Decision-Tree/dataset1/test_set.csv";//args[4];
		String toPrint = "yes";//args[5];

		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> trainingData;
		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> validationData;
		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> testData;

		trainingData = readFile(trainingFile);
		validationData = readFile(validationFile);
		testData = readFile(testFile);

		// Logic for Information Gain Heuristic:
		ENTROPY = true;
		DecisionTree treeIG = new DecisionTree((List<String>) Arrays.asList(featureName));
		Node rootIG = new Node();
		rootIG.setParent(null);
		rootIG = treeIG.createTree(trainingData, rootIG);
		if (toPrint.equalsIgnoreCase("yes")) {
			System.out.println("===Decision Tree using Information Gain Heuristic===\n");
			System.out.println(treeIG.printTree(rootIG, 0));
		}
		System.out.println("===Accuracy for Information Gain Heuristic===");
		System.out.println("\tBefore Pruning: Accuracy= " + treeIG.calculateAccuracy(rootIG, testData) +"%");
		
		/* Information Gain Heuristic- Post Pruning */
		Node prunedIG = treeIG.prune(L,K,rootIG,validationData);
		System.out.println("\tAfter Pruning: Accuracy= " + treeIG.calculateAccuracy(prunedIG, testData) +"%\n\n");
			
		ENTROPY = false;
		DecisionTree treeVI = new DecisionTree((List<String>) Arrays.asList(featureName));
		Node rootVI = new Node();
		rootVI.setParent(null);
		rootVI = treeVI.createTree(trainingData, rootVI);

		if (toPrint.equalsIgnoreCase("yes")) {
			System.out.println("===Decision Tree using Impurity Gain Heuristic===\n");
			System.out.println(treeVI.printTree(rootVI, 0));
		}
		System.out.println("===Accuracy for Variance Impurity Heuristic===");
		System.out.println("\tBefore Pruning: Accuracy= " + treeVI.calculateAccuracy(rootVI, testData) + "%");
		/*Pruning for Variance Impurity Heuristic */
		Node prunedVI = treeIG.prune(L,K,rootVI,validationData);
		System.out.println("\tAfter Pruning: Accuracy= " + treeIG.calculateAccuracy(prunedVI, testData) +"%");
		
	}

	/**
	 * @param file : Name of the file to read
	 * @return
	 */
	private static LinkedHashMap<String, LinkedHashMap<Integer, Integer>> readFile(String file) {
		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> data = new LinkedHashMap<String, LinkedHashMap<Integer, Integer>>();

		FileReader fr = null;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			if ((line = br.readLine()) != null) {
				featureName = line.split(SPLIT_SYMBOL);
				for (int i = 0; i < featureName.length; i++) {
					data.put(featureName[i], new LinkedHashMap<Integer, Integer>());
				}
			}
			int index = 2;
			while ((line = br.readLine()) != null) {
				String arr[] = line.split(SPLIT_SYMBOL);
				for (int i = 0; i < arr.length; i++) {
					data.get(featureName[i]).put(index, Integer.parseInt(arr[i]));
				}
				index++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
