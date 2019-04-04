package com.data.preprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class VocabProcessor {
	private static Map<String, Integer> vocabMap = new LinkedHashMap<>();

	public static Map<String, Integer> getVocabMap() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new FileReader("/Users/vpati/Intuit_project/ml/lda/LDAAVIIssue/src/test/resources/vocab.kos.txt"));
			String line;
			line = br.readLine();
			int i = 1;
			while (line != null) {
				vocabMap.put(line.toLowerCase(), i++);
				line = br.readLine();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return vocabMap;
	}
}
