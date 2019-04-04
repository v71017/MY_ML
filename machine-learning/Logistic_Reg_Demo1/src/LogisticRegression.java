import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @author Ambar
 *
 */
public class LogisticRegression {

	public static final String SPAM_CLASS = "spam";
	public static final String HAM_CLASS = "ham";
	public static final String CLASS = "CLASS";
	public static final String SEPERATOR = " ";
	private static final int SPAM = -1;
	private static final int HAM = -2;

	public static double eta;
	public static double lambda;
	public static int iterations;

	private int total; /* Total number of documents */
	private int nHam; /* Number of HAM documents */
	private int nSpam; /* Number of SPAM documents */

	ArrayList<String> classes = new ArrayList<String>();
	// Prob will be of size of total examples
	ArrayList<Double> prob = new ArrayList<Double>();
	// Weights will be of size of vocab.
	ArrayList<Double> weights = new ArrayList<Double>();

	/* If we are using the stop words list for optimization */
	boolean usingStopWords = false;

	/* DS to hold the data */
	LinkedHashSet<String> stopWords = new LinkedHashSet<String>();
	LinkedHashSet<String> vocab = new LinkedHashSet<String>();

	ArrayList<HashMap<String, Integer>> data = new ArrayList<HashMap<String, Integer>>();

	/**
	 * Constructor when not using stopwords
	 * 
	 * @throws IOException
	 */
	public LogisticRegression(String trainingHamDir, String trainingSpamDir)
			throws IOException {
		createVocab(trainingHamDir, trainingSpamDir);

		this.nHam = readHamDir(trainingHamDir);
		this.nSpam = readSpamDir(trainingSpamDir);
		this.total = nHam + nSpam;
		classes.add(HAM_CLASS);
		classes.add(SPAM_CLASS);
	}

	/**
	 * Constructor when not using stopwords
	 * 
	 * @throws IOException
	 */
	public LogisticRegression(String trainingHamDir, String trainingSpamDir,
			String stopWords) throws IOException {
		this.usingStopWords = true;
		readStopWords(stopWords);
		createVocab(trainingHamDir, trainingSpamDir);

		this.nHam = readHamDir(trainingHamDir);
		this.nSpam = readSpamDir(trainingSpamDir);
		this.total = nHam + nSpam;

		classes.add(HAM_CLASS);
		classes.add(SPAM_CLASS);
	}

	private void createVocab(String trainingHamDir, String trainingSpamDir)
			throws IOException {
		File hamDir = new File(trainingHamDir);
		String line = null;
		for (File hFile : hamDir.listFiles()) {
			FileReader fr = new FileReader(hFile);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String list[] = line.split(SEPERATOR);
				for (String word : list) {
					if (usingStopWords && stopWords.contains(word)) {
						// Do Nothing
					} else {
						this.vocab.add(word);
					}
				}
			}
			br.close();
		}

		File spamDir = new File(trainingSpamDir);
		for (File sFile : spamDir.listFiles()) {
			FileReader fr = new FileReader(sFile);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String list[] = line.split(SEPERATOR);
				for (String word : list) {
					if (usingStopWords && (stopWords.contains(word))) {
						// Do Nothing
					} else {
						vocab.add(word);
					}
				}
			}
			br.close();
		}

	}

	/**
	 * Reads the stop words
	 * 
	 * @param stopWords
	 * @throws IOException
	 */
	private void readStopWords(String stopWords) throws IOException {
		File file = new File(stopWords);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			this.stopWords.add(line);
		}
		br.close();
	}

	/**
	 * This method trains the model according to the data.
	 */
	public void trainLR() {
		for (int i = 0; i < vocab.size(); i++) {
			weights.add(0.0);
		}
		for (int iter = 0; iter < iterations; iter++) {

			for (int i = 0; i < total; i++) {
				HashMap<String, Integer> example = data.get(i);
				String computedClass = example.get(CLASS) == HAM ? HAM_CLASS
						: SPAM_CLASS;

				prob.add(compute(example, computedClass));
			}
			for (int i = 0; i < total; i++) {
				HashMap<String, Integer> example = data.get(i);

				for (String word : vocab) {
					double rowClass = (example.get(CLASS) == HAM) ? 1 : 0;
					double delta;
					delta = weights.get(i) + example.get(word)
							* (rowClass - prob.get(i));
					// L2 regularization
					double val = weights.get(i)
							+ eta
							* (-delta - (lambda * weights.get(i) * weights
									.get(i)));
					weights.set(i, val);
				}
			}
		}
		data = null;
	}

	private Double compute(HashMap<String, Integer> row, String computedClass) {
		double prob = weights.get(0);
		int i = 0;
		for (String word : vocab) {
			prob = prob + (weights.get(i++) * ((double) row.get(word)));
		}
		double result = 1 / ((double) 1 + Math.exp(prob));

		if (computedClass.equalsIgnoreCase(SPAM_CLASS)) {
			return result;
		} else
			return (1 - result);
	}

	/**
	 * Prints the individual accuracy for HAM and SPAM
	 * 
	 * @return The overall accuracy.
	 * @throws IOException
	 */
	public double calculateAccuracy(String testingHamDir, String testingSpamDir)
			throws IOException {
		File hamDir = new File(testingHamDir);
		double hamAccuracy = 0, hamTotal = 0;
		double spamAccuracy = 0, spamTotal = 0;
		for (File doc : hamDir.listFiles()) {
			String result = applyLR(doc, HAM_CLASS);
			if (result.equals(HAM_CLASS)) {
				hamAccuracy++;
			}
			hamTotal++;
		}
	//	System.out.println("\tAccuracy for Ham Class=" + (hamAccuracy / hamTotal * 100) + "%");

		File spamDir = new File(testingSpamDir);
		for (File doc : spamDir.listFiles()) {
			String result = applyLR(doc, SPAM_CLASS);
			if (result.equals(SPAM_CLASS)) {
				spamAccuracy++;
			}
			spamTotal++;
		}
		//System.out.println("\tAccuracy for Spam Class=" + (spamAccuracy / spamTotal * 100) + "%");
		return ((hamAccuracy + spamAccuracy) / (spamTotal + hamTotal) * 100);
	}

	/**
	 * Predict the class for the given document.
	 * 
	 * @param hamClass
	 * @return The predicted class
	 * @throws IOException
	 */
	private String applyLR(File doc, String hamClass) throws IOException {
		HashMap<String, Integer> row = new HashMap<String, Integer>();
		// Adding vocab in row.
		for (String word : vocab) {
			row.put(word, 0);
		}

		FileReader fr = new FileReader(doc);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null) {
			String[] list = line.split(SEPERATOR);
			for (String dw : list) {
				if (vocab.contains(dw))
					row.put(dw, row.get(dw) + 1);
			}
		}
		br.close();

		return (compute(row, HAM_CLASS) > compute(row, SPAM_CLASS)) ? HAM_CLASS
				: SPAM_CLASS;
	}

	/**
	 * Reads the SPAM Directory.
	 * 
	 * @throws IOException
	 */
	private int readSpamDir(String trainingSpamDir) throws IOException {
		File spamDir = new File(trainingSpamDir);
		int nSpam = 0;
		String line = null;
		for (File sFile : spamDir.listFiles()) {
			HashMap<String, Integer> row = new HashMap<String, Integer>();
			// Adding vocab in row.
			for (String word : vocab) {
				row.put(word, 0);
			}
			FileReader fr = new FileReader(sFile);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String list[] = line.split(SEPERATOR);
				for (String word : list) {
					if (usingStopWords && (stopWords.contains(word))) {
						// Do Nothing
					} else {
						row.put(word, row.get(word) + 1);
					}
				}
			}
			br.close();
			row.put(CLASS, SPAM);
			data.add(row);
			nSpam++;
		}
		return nSpam;
	}

	/**
	 * Reads the HAM Directory.
	 * 
	 * @throws IOException
	 */

	private int readHamDir(String trainingHamDir) throws IOException {
		File hamDir = new File(trainingHamDir);
		int nHam = 0;
		String line = null;

		for (File hFile : hamDir.listFiles()) {
			HashMap<String, Integer> row = new HashMap<String, Integer>();
			FileReader fr = new FileReader(hFile);
			BufferedReader br = new BufferedReader(fr);
			// Adding vocab in row.
			for (String word : vocab) {
				row.put(word, 0);
			}

			while ((line = br.readLine()) != null) {
				String list[] = line.split(SEPERATOR);
				for (String word : list) {
					if (usingStopWords && stopWords.contains(word)) {
						// Do Nothing
					} else {
						row.put(word, row.get(word) + 1);
					}
				}
			}
			br.close();
			row.put(CLASS, HAM);
			data.add(row);
			nHam++;
		}

		return nHam;
	}

	/**
	 * For debugging purpose to print the weights
	 * 
	 * @param name
	 */
	@SuppressWarnings("unused")
	private void printWeights(String name) {
		System.out.println(name + "\n");
		try {
			PrintWriter writer;
			writer = new PrintWriter(name, "UTF-8");
			for (int i = 0; i < weights.size(); i++) {
				writer.println(weights.get(i));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
