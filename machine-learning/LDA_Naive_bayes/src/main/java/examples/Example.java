package examples;

import java.util.List;

import lda.LDA;
import static lda.inference.InferenceMethod.*;

import org.apache.commons.lang3.tuple.Pair;

import dataset.Dataset;

public class Example {
	public static void main(String[] args) throws Exception {
		Dataset dataset = new Dataset(
				"/Users/vpati/Intuit_project/ml/lda/LDAAVIIssue/src/test/resources/docword.kos.txt",
				"/Users/vpati/Intuit_project/ml/lda/LDAAVIIssue/src/test/resources/vocab.kos.txt");

		final int numTopics = 3;
		LDA lda = new LDA(0.1, 0.1, numTopics, dataset, CGS);
		lda.run();
		System.out.println(lda.computePerplexity(dataset));

		for (int t = 0; t < numTopics; ++t) {
			List<Pair<String, Double>> highRankVocabs = lda.getVocabsSortedByPhi(t);
			System.out.print("t" + t + ": ");
			for (int i = 0; i < 2; ++i) {
				System.out.print("[" + highRankVocabs.get(i).getLeft() + "," + highRankVocabs.get(i).getRight() + "],");
			}
			System.out.println();
		}
	}
}
