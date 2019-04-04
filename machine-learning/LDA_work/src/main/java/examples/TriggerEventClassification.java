package examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import lda.LDA;
import static lda.inference.InferenceMethod.*;

import org.apache.commons.lang3.tuple.Pair;

import com.naivebayes.NaiveBayesClassifier;

import dataset.Dataset;

public class TriggerEventClassification {
	public static void main(String[] args) throws Exception {

		TriggerEventClassification triggerEventClassification = new TriggerEventClassification();
		triggerEventClassification.predictEventType("PI:Network:avi.Service");

	}

	public String predictEventType(String inputKeyword) throws Exception {
		String returnPrediction = "";
		Dataset dataset = new Dataset(
				"/Users/vpati/Intuit_project/ml/lda/LDAAVIIssue/src/test/resources/docword_test.kos.txt",
				"/Users/vpati/Intuit_project/ml/lda/LDAAVIIssue/src/test/resources/vocab.kos.txt");

		final int numTopics = 3;
		final int matrixColumnCount = 15;
		int ldaIterationCount = 5;

		Map<String, Set<String>> topicWordMatrix = new HashMap<>();
		List<String> topicLabel = new ArrayList<>();
		topicLabel.add("f5");
		topicLabel.add("avi");
		topicLabel.add("aci");
		for (int i = 0; i < ldaIterationCount; i++) {
			LDA lda = new LDA(0.5, 0.5, numTopics, dataset, CGS);
			lda.run();
			System.out.println(lda.computePerplexity(dataset));
			for (int t = 0; t < numTopics; ++t) {
				List<Pair<String, Double>> highRankVocabs = lda.getVocabsSortedByPhi(t);
				System.out.print("t" + t + ": ");
				for (int j = 0; j < matrixColumnCount; ++j) {
					System.out.print(
							"[" + highRankVocabs.get(j).getLeft() + "," + highRankVocabs.get(j).getRight() + "],");
					String keyword = highRankVocabs.get(j).getLeft();
					for (String str : topicLabel) {
						if (keyword.contains(str)) {
							if (topicWordMatrix.get(str) == null) {
								Set<String> dataSet = new HashSet<>();
								dataSet.add(keyword);
								topicWordMatrix.put(str, dataSet);
							} else {
								Set<String> dataSet = topicWordMatrix.get(str);
								dataSet.add(keyword);
							}
						}
					}
				}
				System.out.println();
			}
		}

		System.out.println("\n\nTopic matrix::" + topicWordMatrix);
		List<Integer> setSize = new ArrayList<>();
		for (Map.Entry<String, Set<String>> entry : topicWordMatrix.entrySet()) {
			setSize.add(entry.getValue().size());
		}
		int maxSize = Collections.max(setSize);

		// call Naive Bayes classifier
		List<String> X = new ArrayList<String>();
		List<String> y = new ArrayList<String>();
		for (Map.Entry<String, Set<String>> entry : topicWordMatrix.entrySet()) {
			StringBuffer sb = new StringBuffer();
			for (String str : entry.getValue()) {
				sb.append(str).append(" ");
			}
			X.add(sb.toString());
			y.add(entry.getKey());
		}
		NaiveBayesClassifier emNaiveBayes = new NaiveBayesClassifier();
		emNaiveBayes.fit(X, y);

		// Test the naive bayes
		List<String> predictX = new ArrayList<String>();
		List<String> predictY = new ArrayList<String>();
		predictX.add(inputKeyword);
		predictY.add("-1");

		for (int i = 0; i < predictX.size(); i++) {
			Entry<String, Double> result = emNaiveBayes.predict(predictX.get(i));
			returnPrediction = result.getKey();
			System.out.println(result.getKey());
		}
		return returnPrediction;

	}

}
