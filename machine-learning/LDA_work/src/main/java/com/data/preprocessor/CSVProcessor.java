package com.data.preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;

public class CSVProcessor {

	// doc1 - aci
	// doc2 - avi
	// doc3 - f5

	private String baseDataDir = "/Users/vpati/Downloads/Alerts_File";
	private String outputFilePath = "/Users/vpati/Intuit_project/ml/lda/LDAAVIIssue/src/test/resources/docword_test.kos.txt";

	public static void main(String args[]) {
		CSVProcessor csvProcessor = new CSVProcessor();
		csvProcessor.processCSVInput();
	}

	public void processCSVInput() {
		Map<String, Integer> vocabMap = VocabProcessor.getVocabMap();
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			// File write
			File folder = new File(baseDataDir);
			File[] listOfFiles = folder.listFiles();
			for (File name : listOfFiles) {
				if (name.getName().contains(".DS_Store")) {
					ArrayUtils.removeElement(listOfFiles, name);
				}
			}

			fw = new FileWriter(outputFilePath);
			bw = new BufferedWriter(fw);
			bw.write(String.valueOf(listOfFiles.length));
			bw.write("\n");
			bw.write(String.valueOf(vocabMap.size()));
			bw.write("\n");
			bw.write(String.valueOf(vocabMap.size()));
			bw.write("\n");
			int documentId = 0;
			for (File fileName : listOfFiles) {
				deleteFile(baseDataDir);
				if (fileName.getAbsolutePath().contains(".DS_Store"))
					continue;
				documentId++;
				Map<String, Integer> fileWordCountMap = getWordMapForFile(fileName.getAbsolutePath());
				List<String> rowEntry = new ArrayList<>();
				for (Map.Entry<String, Integer> entry : vocabMap.entrySet()) {
					StringBuffer sb = new StringBuffer();
					if (fileWordCountMap.get(entry.getKey()) != null) {
						sb.append(documentId).append(" ").append(entry.getValue()).append(" ")
								.append(String.valueOf(fileWordCountMap.get(entry.getKey())));
						rowEntry.add(sb.toString());
					}

				}

				for (String str : rowEntry) {
					bw.write(str);
					bw.write("\n");
				}

			}
		} catch (Exception e) {
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileReader !!!");
				e.printStackTrace();
			}
		}

	}

	public Map<String, Integer> getWordMapForFile(String fileName) throws IOException {
		Map<String, Integer> vocabMap = VocabProcessor.getVocabMap();

		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(new FileReader(fileName));
		String inputLine = null;
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		try {
			while ((inputLine = bufferedReader.readLine()) != null) {
				String[] words = inputLine.split("[ \n\t\r]");
				for (int wordCounter = 0; wordCounter < words.length; wordCounter++) {
					String key = words[wordCounter].toLowerCase();
					if (vocabMap.containsKey(key)) {
						if (key.length() > 0) {
							if (map.get(key) == null) {
								map.put(key, 1);
							} else {
								int value = map.get(key).intValue();
								value++;
								map.put(key, value);
							}
						}
						Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
						for (Map.Entry<String, Integer> entry : entrySet) {
							System.out.println(entry.getValue() + "\t" + entry.getKey());
						}
					}
				}
			}
		} catch (IOException error) {
			System.out.println("Invalid File");
		} finally {
			bufferedReader.close();
		}
		return map;
	}

	public void deleteFile(String basePath) {
		try {
			File file = new File(basePath + "/" + ".DS_Store");
			if (file.exists()) {
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");
				}
			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
}