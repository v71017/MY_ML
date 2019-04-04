package salimm.benchmark.objs;

import java.util.HashMap;

import salimm.benchmark.types.ParamType;

/**
 * Will contain the prediction scores, parameters selected and labels predicted.
 * 
 * @author salimm
 *
 */
public class BenchmarkClassificationResult extends BenchmarkResults{

	private double[] probabilities;
	private int[] predictions;
	private HashMap<ParamType, Object> params;

	public BenchmarkClassificationResult(double[] probabilities, int[] predictions, HashMap<ParamType, Object> params) {
		this.setProbabilities(probabilities);
		this.setPredictions(predictions);
		this.setParams(params);
	}

	public BenchmarkClassificationResult(double[] probabilities, int[] predictions) {
		this(probabilities, predictions, new HashMap<ParamType, Object>());
	}

	public double[] getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(double[] probabilities) {
		this.probabilities = probabilities;
	}

	public int[] getPredictions() {
		return predictions;
	}

	public void setPredictions(int[] predictions) {
		this.predictions = predictions;
	}

	public HashMap<ParamType, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<ParamType, Object> params) {
		this.params = params;
	}
}
