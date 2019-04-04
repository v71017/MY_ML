package salimm.benchmark;

import salimm.benchmark.loader.CSVLoader;
import salimm.benchmark.objs.BenchmarkDataTrainTest;
import salimm.benchmark.svm.LibSVMRunner;

public class BenchmarkMain {
	public static void main(String[] args) throws Exception {

		BenchmarkEngine engine = new BenchmarkEngine();
		engine.getRunners().add(new LibSVMRunner());

		engine.getDatasets().add(new BenchmarkDataTrainTest(CSVLoader.load("data/heart.csv", -1, "HEART"), 0.65));

		engine.start();

	}
}
