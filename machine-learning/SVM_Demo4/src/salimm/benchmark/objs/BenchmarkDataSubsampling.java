package salimm.benchmark.objs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import salimm.benchmark.utils.Pair;

public class BenchmarkDataSubsampling extends BenchmarkData {

	private BenchmarkDataSingle data;
	private int K;
	private int seed;
	private double p;

	private List<Pair<List<Integer>, List<Integer>>> subsamples;

	public BenchmarkDataSubsampling(BenchmarkDataSingle data, int K, double p, String name) {
		this(data, K, p, name, 1);
		subsamples = new ArrayList<Pair<List<Integer>, List<Integer>>>();
	}

	public BenchmarkDataSubsampling(BenchmarkDataSingle data, int K, double p, String name, int seed) {
		super(name,data.getTotalNumberOfClasses());
		this.setP(p);
		this.setData(data);
		this.K = K;
		this.seed = seed;
		createSubSamples();
	}

	public void createSubSamples() {
		int N = data.size();

		Random rn = new Random();
		rn.setSeed(seed);

		int trainNum = (int) ((int) N * p);
		ArrayList<Integer> originalIndicies = new ArrayList<Integer>(
				IntStream.of(IntStream.rangeClosed(1, 10).toArray()).boxed().collect(Collectors.toList()));
		for (int k = 0; k < K; k++) {
			@SuppressWarnings("unchecked")
			ArrayList<Integer> shuffledIdx = (ArrayList<Integer>) originalIndicies.clone();
			Collections.shuffle(shuffledIdx);
			List<Integer> trainIdx = shuffledIdx.subList(0, trainNum);
			List<Integer> testIdx = shuffledIdx.subList(trainNum, N - 1);
			subsamples.add(new Pair<List<Integer>, List<Integer>>(trainIdx, testIdx));
		}
	}

	public BenchmarkDataSingle getData() {
		return data;
	}

	public void setData(BenchmarkDataSingle data) {
		this.data = data;
	}

	public int getK() {
		return K;
	}

	public void setK(int K) {
		this.K = K;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public BenchmarkDataSingle train(int i) {
		return data.subset(subsamples.get(i).getFirst());
	}

	public BenchmarkDataSingle test(int i) {
		return data.subset(subsamples.get(i).getSecond());
	}

}
