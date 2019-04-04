package salimm.benchmark.svm;

import java.util.HashMap;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import salimm.benchmark.AlgorithmRunner;
import salimm.benchmark.evals.StatGeneratorUtils;
import salimm.benchmark.objs.BenchmarkClassificationResult;
import salimm.benchmark.objs.BenchmarkDataSingle;
import salimm.benchmark.objs.BenchmarkDataSubsampling;
import salimm.benchmark.objs.BenchmarkStats;
import salimm.benchmark.types.ParamType;

public class LibSVMRunner extends AlgorithmRunner {

	@Override
	public BenchmarkStats runTrainTest(BenchmarkDataSingle train, BenchmarkDataSingle test) {
		svm_problem prob = new svm_problem();
		int recordCount = train.size();
		int featureCount = train.featureSize();
		prob.y = new double[recordCount];
		prob.l = recordCount;
		prob.x = new svm_node[recordCount][featureCount];

		for (int i = 0; i < recordCount; i++) {
			double[] features = train.features(i);
			prob.x[i] = new svm_node[features.length];
			for (int j = 0; j < features.length; j++) {
				svm_node node = new svm_node();
				node.index = j;
				node.value = features[j];
				prob.x[i][j] = node;
			}
			prob.y[i] = train.label(i);
		}

		svm_parameter param = new svm_parameter();
		param.probability = 1;
		param.gamma = 0.5;
		param.nu = 0.5;
		param.C = 0.01;
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.LINEAR;
		param.cache_size = 20000;
		param.eps = 0.01;

		svm_model model = svm.svm_train(prob, param);

		BenchmarkClassificationResult result = svmPredict(test, model);

		return StatGeneratorUtils.genClassificationStats(result);
	}

	static BenchmarkClassificationResult svmPredict(BenchmarkDataSingle data, svm_model model) {

		double[] yProb = new double[data.size()];
		int[] yPred = new int[data.size()];

		for (int k = 0; k < data.size(); k++) {

			double[] fVector = data.features(k);

			svm_node[] nodes = new svm_node[fVector.length];
			for (int i = 0; i < fVector.length; i++) {
				svm_node node = new svm_node();
				node.index = i;
				node.value = fVector[i];
				nodes[i] = node;
			}

			int[] labels = new int[data.getTotalNumberOfClasses()];
			svm.svm_get_labels(model, labels);

			double[] prob_estimates = new double[data.getTotalNumberOfClasses()];
			yPred[k] = (int) svm.svm_predict_probability(model, nodes, prob_estimates);
			yProb[k] = prob_estimates[1];

		}

		HashMap<ParamType, Object> params = new HashMap<ParamType, Object>();
		params.put(ParamType.SVM_COST, new Double(model.param.C));
		return new BenchmarkClassificationResult(yProb, yPred, params);
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public BenchmarkStats runSubSampling(BenchmarkDataSubsampling data) {
		return null;
	}

}
