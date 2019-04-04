import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.swing.text.AbstractDocument.BranchElement;

/**
 * @author ambar
 * 		Class which models the multinomial Naive Bayes Classification model 
 * 		with one-Laplace Smoothing
 */
public class MultinomialNB {

	public static final String SPAM_CLASS = "spam";
	public static final String HAM_CLASS = "ham";
	public static final String SEPERATOR = " ";

	int total; 	/* Total number of documents */
	int nHam; 	/* Number of HAM documents */
	int nSpam;  /* Number of SPAM documents */

	ArrayList<String> classes = new ArrayList<String>();
	HashMap<String, Double> prior = new HashMap<String, Double>();

	HashMap<String, HashMap<String, Double>> condProb = new HashMap<String, HashMap<String, Double>>();
	
	/* If we are using the stop words list for optimization*/
	boolean usingStopWords = false;
	
	/* DS to hold the data*/
	LinkedHashSet<String> stopWords = new LinkedHashSet<String>();
	LinkedHashSet<String> vocab = new LinkedHashSet<String>();
	HashMap<String, Integer> hamText = new HashMap<String, Integer>();
	HashMap<String, Integer> spamText = new HashMap<String, Integer>();

	
	/**
	 * 		Constructor when not using stopwords 
	 * @throws IOException
	 */
	public MultinomialNB(String trainingHamDir, String trainingSpamDir) throws IOException {
		this.nHam = readHamDir(trainingHamDir);
		this.nSpam = readSpamDir(trainingSpamDir);
		this.total = nHam + nSpam;

		classes.add(HAM_CLASS);
		classes.add(SPAM_CLASS);
		// Setting the prior
		prior.put(HAM_CLASS, (double) nHam / (double) total);
		prior.put(SPAM_CLASS, (double) nSpam / (double) total);

		condProb = new HashMap<String, HashMap<String, Double>>();
		condProb.put(HAM_CLASS, new HashMap<String, Double>());
		condProb.put(SPAM_CLASS, new HashMap<String, Double>());
	}

	/**
	 * 		Constructor when not using stopwords 
	 * @throws IOException
	 */
	public MultinomialNB(String trainingHamDir, String trainingSpamDir, String stopWords) throws IOException {
		this.usingStopWords = true;
		readStopWords(stopWords);
	
		this.nHam = readHamDir(trainingHamDir);
		this.nSpam = readSpamDir(trainingSpamDir);
		this.total = nHam + nSpam;

		classes.add(HAM_CLASS);
		classes.add(SPAM_CLASS);
		prior.put(HAM_CLASS, (double) nHam / (double) total);
		prior.put(SPAM_CLASS, (double) nSpam / (double) total);

		condProb = new HashMap<String, HashMap<String, Double>>();
		condProb.put(HAM_CLASS, new HashMap<String, Double>());
		condProb.put(SPAM_CLASS, new HashMap<String, Double>());
	}

	/**
	 * 		Reads the stop words
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
	public void trainNB() {
		for (String outputClass : classes) {
			for (String term : vocab) {
				double total = 0;
				if (outputClass.equalsIgnoreCase(HAM_CLASS)) {
					for (String key : hamText.keySet()) {
						total += hamText.get(key);
					}
				} else {
					for (String key : spamText.keySet()) {
						total += spamText.get(key);
					}
				}
				//adding vocab.size in total as a part of Laplace one smoothing.
				total = total + vocab.size();

				if (outputClass.equalsIgnoreCase(HAM_CLASS)) {
					if (hamText.containsKey(term)) {
						condProb.get(HAM_CLASS).put(term, ((double) (hamText.get(term) + 1)) / total);

					} else {
						condProb.get(HAM_CLASS).put(term, ((double) (1)) / total);
					}
				} else if (outputClass.equalsIgnoreCase(SPAM_CLASS)) {
					if (spamText.containsKey(term)) {
						condProb.get(SPAM_CLASS).put(term, ((double) (spamText.get(term) + 1)) / total);
					} else {
						condProb.get(SPAM_CLASS).put(term, ((double) (1)) / total);
					}
				}
			}

		}
	}

	/**
	 * 		Prints the individual accuracy for HAM and SPAM
	 * @return The overall accuracy.
	 * @throws IOException
	 */
	public double calculateAccuracy(String testingHamDir, String testingSpamDir) throws IOException {
		File hamDir = new File(testingHamDir);
		double hamAccuracy = 0, hamTotal = 0;
		double spamAccuracy = 0, spamTotal = 0;
		for (File doc : hamDir.listFiles()) {
			String result = applyNB(doc);
			if (result.equals(HAM_CLASS)) {
				hamAccuracy++;
			}
			hamTotal++;
		}
		System.out.println("\tAccuracy for Ham Class=" + (hamAccuracy / hamTotal * 100) + "%");

		File spamDir = new File(testingSpamDir);
		for (File doc : spamDir.listFiles()) {
			String result = applyNB(doc);
			if (result.equals(SPAM_CLASS)) {
				spamAccuracy++;
			}
			spamTotal++;
		}
		System.out.println("\tAccuracy for Spam Class=" + (spamAccuracy / spamTotal * 100) + "%");

		return ((hamAccuracy + spamAccuracy) / (spamTotal + hamTotal) * 100);
	}

	/**
	 * 		Predict the class for the given document.
	 * @return The predicted class
	 * @throws IOException
	 */
	private String applyNB(File doc) throws IOException {
		LinkedHashSet<String> words = new LinkedHashSet<String>();
		double[] score = new double[classes.size()];
		FileReader fr = new FileReader(doc);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null) {
			String[] list = line.split(SEPERATOR);
			for (String dw : list) {
				words.add(dw);
			}
		}
		br.close();

		for (String outputClass : classes) {
			score[classes.indexOf(outputClass)] = Math.log(prior.get(outputClass));
			for (String term : vocab) {
				if (words.contains(term)) {
					double val = condProb.get(outputClass).get(term);
					score[classes.indexOf(outputClass)] += Math.log(val);
				}
			}
		}
		String res = (score[classes.indexOf(HAM_CLASS)] > score[classes.indexOf(SPAM_CLASS)]) ? HAM_CLASS : SPAM_CLASS;
		return res;
	}

	/**
	 * 		Reads the SPAM Directory.
	 * @throws IOException
	 */
	private int readSpamDir(String trainingSpamDir) throws IOException {
		File spamDir = new File(trainingSpamDir);
		int nSpam = 0;
		String line = null;
		for (File sFile : spamDir.listFiles()) {
			FileReader fr = new FileReader(sFile);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String list[] = line.split(SEPERATOR);
				for (String word : list) {
					if (usingStopWords && (stopWords.contains(word))) {
						//Do Nothing
					} else {
						vocab.add(word);
						if (spamText.containsKey(word)) {
							spamText.put(word, spamText.get(word) + 1);
						} else {
							spamText.put(word, 1);
						}
					}
				}
			}
			br.close();
			nSpam++;
		}
		return nSpam;
	}

	/**
	 * 		Reads the HAM Directory.
	 * @throws IOException
	 */
	
	private int readHamDir(String trainingHamDir) throws IOException {
		File hamDir = new File(trainingHamDir);
		int nHam = 0;
		String line = null;

		for (File hFile : hamDir.listFiles()) {
			FileReader fr = new FileReader(hFile);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				String list[] = line.split(SEPERATOR);
				for (String word : list) {
					if (usingStopWords && stopWords.contains(word)) {
						//Do Nothing
					} else {
						this.vocab.add(word);
						if (hamText.containsKey(word)) {
							hamText.put(word, hamText.get(word) + 1);
						} else {
							hamText.put(word, 1);
						}
					}
				}
			}
			br.close();
			nHam++;
		}

		return nHam;
	}

}
