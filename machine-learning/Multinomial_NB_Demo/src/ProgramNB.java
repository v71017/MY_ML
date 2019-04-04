import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @author ambar
 *
 */
public class ProgramNB {

	/**
	 * This function starts the NB algorithm.
	 * 
	 * @param args
	 *            has list on arguments.
	 */
	public static void main(String[] args) {
		/*
		 * String trainingHamDir = "D:\\Courses\\ML-580L\\HW2\\train\\ham";
		 * String trainingSpamDir = "D:\\Courses\\ML-580L\\HW2\\train\\spam";
		 * String testingHamDir = "D:\\Courses\\ML-580L\\HW2\\test\\ham"; String
		 * testingSpamDir ="D:\\Courses\\ML-580L\\HW2\\test\\spam"; String
		 * stopWord = "D:\\Courses\\ML-580L\\HW2\\stopWords.txt";
		 */
//		if (args.length != 5) {
//			System.out.println("Invalid Arguments: Please refer the README.txt for instructions");
//			return;
//		}

		String trainingHamDir = "/Users/vpati/Intuit_project/ml/naive/Naive-Bayes/train/ham";// args[0];
		String trainingSpamDir = "/Users/vpati/Intuit_project/ml/naive/Naive-Bayes/train/spam";// args[1];
		String testingHamDir = "/Users/vpati/Intuit_project/ml/naive/Naive-Bayes/test/ham";// args[2];
		String testingSpamDir = "/Users/vpati/Intuit_project/ml/naive/Naive-Bayes/test/spam";// args[3];
		String stopWord = "/Users/vpati/Intuit_project/ml/naive/Naive-Bayes/stopWords.txt";// args[4];

		try {
			System.out.println("===Training started without using stopWords===");
			MultinomialNB multiNB = new MultinomialNB(trainingHamDir, trainingSpamDir);
			multiNB.trainNB();
			System.out.println("Training successful!");

			double accuracy = multiNB.calculateAccuracy(testingHamDir, testingSpamDir);
			System.out.println("Overall accuracy without using stopwords = " + accuracy + "%\n\n");

			System.out.println("===Training started using stopWords===");
			MultinomialNB stopWordmultiNB = new MultinomialNB(trainingHamDir, trainingSpamDir, stopWord);
			stopWordmultiNB.trainNB();
			System.out.println("Trained successful!");

			double accuracySW = stopWordmultiNB.calculateAccuracy(testingHamDir, testingSpamDir);
			System.out.println("Overall Accuracy using stopwords = " + accuracySW + "%");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
