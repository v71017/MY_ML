package salimm.benchmark;

import salimm.benchmark.objs.BenchmarkData;
import salimm.benchmark.objs.BenchmarkDataSingle;
import salimm.benchmark.objs.BenchmarkDataSubsampling;
import salimm.benchmark.objs.BenchmarkDataTrainTest;
import salimm.benchmark.objs.BenchmarkStats;

/**
 * Class used for running a specific algorithm
 * 
 * @author salimm
 *
 */
public abstract class AlgorithmRunner {

	public abstract BenchmarkStats runTrainTest(BenchmarkDataSingle train, BenchmarkDataSingle test);

	public abstract BenchmarkStats runSubSampling(BenchmarkDataSubsampling data);

	public BenchmarkStats run(BenchmarkData data) {
		if (data instanceof BenchmarkDataTrainTest)
			return runTrainTest(((BenchmarkDataTrainTest) data).getTrain(), ((BenchmarkDataTrainTest) data).getTest());
		else if (data instanceof BenchmarkDataTrainTest)
			return runSubSampling(((BenchmarkDataSubsampling) data));
		else
			return null;
	}

	public abstract String name();

}
