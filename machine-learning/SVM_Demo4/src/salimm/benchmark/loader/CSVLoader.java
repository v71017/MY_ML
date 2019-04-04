package salimm.benchmark.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import salimm.benchmark.objs.BenchmarkDataSingle;

public class CSVLoader {

	public static BenchmarkDataSingle load(String fileAddress, int labelIdx, String name) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(fileAddress)));
		List<double[]> features = new ArrayList<double[]>();
		List<Integer> labels = new ArrayList<Integer>();
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] parts = line.split(",");

			if (labelIdx == -1)
				labelIdx = parts.length - 1;

			double[] row = new double[parts.length - 1];

			int idx = 0;
			for (int i = 0; i < parts.length; i++) {
				if (i == labelIdx)
					labels.add(new Integer(parts[i].trim()));
				else {
					row[idx] = Double.parseDouble(parts[i].trim());
					idx++;
				}
			}
			if (features.size() > 0)
				if (row.length != features.get(0).length) {
					br.close();
					throw new Exception("Erro occured, csv file should have eequal number of columns on each row");
				}

			features.add(row);
		}

		br.close();

		return new BenchmarkDataSingle(features.toArray(new double[0][0]),
				labels.stream().mapToInt(Integer::intValue).toArray(), name, (int) labels.stream().distinct().count());

	}
}
