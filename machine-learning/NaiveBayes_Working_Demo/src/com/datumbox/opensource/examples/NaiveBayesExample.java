/* 
 * Copyright (C) 2014 Vasilis Vryniotis <bbriniotis at datumbox.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.datumbox.opensource.examples;

import com.datumbox.opensource.classifiers.NaiveBayes;
import com.datumbox.opensource.dataobjects.NaiveBayesKnowledgeBase;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 * @see <a href=
 *      "http://blog.datumbox.com/developing-a-naive-bayes-text-classifier-in-java/">http://blog.datumbox.com/developing-a-naive-bayes-text-classifier-in-java/</a>
 */
public class NaiveBayesExample {

	/**
	 * Reads the all lines from a file and places it a String array. In each
	 * record in the String array we store a training example text.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String[] readLines(String fileName) throws IOException {

		// Reader fileReader = new InputStreamReader(url.openStream(),
		// Charset.forName("UTF-8"));
		// List<String> lines;
		// try (BufferedReader bufferedReader = new BufferedReader(fileReader))
		// {
		// lines = new ArrayList<>();
		// String line;
		// while ((line = bufferedReader.readLine()) != null) {
		// lines.add(line);
		// }
		// }

		BufferedReader br = null;
		String line = "";
		List<String> lines = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return lines.toArray(new String[lines.size()]);
	}

	/**
	 * Main method
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		// map of dataset files
		Map<String, String> trainingFiles = new HashMap<>();
		trainingFiles.put("ACI", "/Users/vpati/Downloads/Alerts_File/ACI_Alerts_ori.csv");
		trainingFiles.put("AVI", "/Users/vpati/Downloads/Alerts_File/avi_alerts_ori.csv");
		trainingFiles.put("F5", "/Users/vpati/Downloads/Alerts_File/F5_Alerts_ori.csv");

		// loading examples in memory
		Map<String, String[]> trainingExamples = new HashMap<>();
		for (Map.Entry<String, String> entry : trainingFiles.entrySet()) {
			trainingExamples.put(entry.getKey(), readLines(entry.getValue()));
		}

		// train classifier
		NaiveBayes nb = new NaiveBayes();
		nb.setChisquareCriticalValue(6.63); // 0.01 pvalue
		nb.train(trainingExamples);

		// get trained classifier knowledgeBase
		NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();

		nb = null;
		trainingExamples = null;

		// Use classifier
		nb = new NaiveBayes(knowledgeBase);
		String exampleEn = "PI:Network:aci.Leaf Switches Status";
		String outputEn = nb.predict(exampleEn);
		System.out.format("The sentense \"%s\" was classified as \"%s\".%n", exampleEn, outputEn);

		String exampleFr = "PI:Network:avi.Cluster Active Members";
		String outputFr = nb.predict(exampleFr);
		System.out.format("The sentense \"%s\" was classified as \"%s\".%n", exampleFr, outputFr);

		String exampleDe = "PI:Network:f5 connections per second";
		String outputDe = nb.predict(exampleDe);
		System.out.format("The sentense \"%s\" was classified as \"%s\".%n", exampleDe, outputDe);

	}

}
