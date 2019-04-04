package salimm.benchmark;

import java.util.ArrayList;
import java.util.List;

import salimm.benchmark.objs.BenchmarkData;
import salimm.benchmark.objs.BenchmarkResults;
import salimm.benchmark.objs.BenchmarkStats;

/**
 * This class is used to benchmarking different algorithms
 * 
 * @author salimm
 *
 */
public class BenchmarkEngine {

	private List<AlgorithmRunner> runners;
	private List<BenchmarkData> datasets;

	/**
	 * engine
	 */
	public BenchmarkEngine() {
		setRunners(new ArrayList<AlgorithmRunner>());
		setDatasets(new ArrayList<BenchmarkData> ());
	}

	public void start() {
		for (BenchmarkData data : getDatasets()) {
			for (AlgorithmRunner runner : getRunners()) {
				BenchmarkStats stat = runner.run(data);
			}
			
		}
	}

	private BenchmarkStats generateStats(BenchmarkResults results) {

		return null;
	}

	public List<AlgorithmRunner> getRunners() {
		return runners;
	}

	public void setRunners(List<AlgorithmRunner> runners) {
		this.runners = runners;
	}

	public List<BenchmarkData> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<BenchmarkData> datasets) {
		this.datasets = datasets;
	}
}
