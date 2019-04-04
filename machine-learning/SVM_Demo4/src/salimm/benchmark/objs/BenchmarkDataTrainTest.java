package salimm.benchmark.objs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Dataset used for train and test experiments
 * 
 * @author salimm
 *
 */
public class BenchmarkDataTrainTest extends BenchmarkData {

	private BenchmarkDataSingle train;
	private BenchmarkDataSingle test;
	private double p;
	private int seed;

	public BenchmarkDataTrainTest(BenchmarkDataSingle data, double P, String name) {
		this(data, P, 1, name);
	}

	public BenchmarkDataTrainTest(BenchmarkDataSingle data, double P) {
		this(data, P, 1, data.getName());
	}

	public BenchmarkDataTrainTest(BenchmarkDataSingle data, double P, int seed, String name) {
		super(name,data.getTotalNumberOfClasses());
		this.seed = seed;
		setP(P);
		generateTrainTest(data);
	}

	private void generateTrainTest(BenchmarkDataSingle data) {
		int N = data.size();

		Random rn = new Random();
		rn.setSeed(seed);

		int trainNum = (int) ((int) N * p);
		ArrayList<Integer> originalIndicies = new ArrayList<Integer>(
				IntStream.of(IntStream.rangeClosed(0, N - 1).toArray()).boxed().collect(Collectors.toList()));
		@SuppressWarnings("unchecked")
		ArrayList<Integer> shuffledIdx = (ArrayList<Integer>) originalIndicies.clone();
		Collections.shuffle(shuffledIdx);
		List<Integer> trainIdx = shuffledIdx.subList(0, trainNum);
		List<Integer> testIdx = shuffledIdx.subList(trainNum, N - 1);

		this.train = data.subset(trainIdx);
		this.test = data.subset(testIdx);
	}

	public BenchmarkDataSingle getTrain() {
		return train;
	}

	public BenchmarkDataSingle getTest() {
		return test;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

}
