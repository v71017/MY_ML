
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.bayes.NaiveBayes;

public class NaiveBayesTest {
	private static NaiveBayes obj = null;
	private static Map<Double, String> classMap;
	static {
		try {
			Instances dataset = new Instances(new BufferedReader(new FileReader(
					"/Users/vpati/Documents/workspace_intuit_ml/WekaNaiveBayes/src/avi_classification.arff")));
			dataset.setClassIndex(dataset.numAttributes() - 1);
			obj = new NaiveBayes();
			obj.buildClassifier(dataset);
			System.out.println(obj);
			classMap = new HashMap<>();
			classMap.put(0.0, "Service engine unable to communicate to controller");
			classMap.put(1.0, "Check underlying ACI infrastructure");
			classMap.put(2.0, "Service engine unable to communicate to controller");
			classMap.put(3.0, "Check SE throughput and number of connections on SE");
			classMap.put(4.0, "Check ESX connectivity");
			classMap.put(5.0, "LLDP lost connectivity");
			classMap.put(6.0, "Engage SD team for Vcenter connectivity");
			classMap.put(7.0,
					"Engage SD team for Vcenter connectivity and Service engine unable to communicate to controller");
			classMap.put(8.0, "ESX host is down and Vcenter is down");
			classMap.put(9.0,
					"Major impact. ESX host is offline and vcenter lost connectivity.Multiple virtual services got impacted");
			classMap.put(10.0,
					"Major impact. ESX host is offline and vcenter lost connectivity.Multiple virtual services got impacted");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		// load dataset
		// DataSource source = new
		// DataSource("/Users/vpati/Documents/workspace_intuit_ml/WekaNaiveBayes/src/avi_classification.arff");

		// Instance last=dataset.lastInstance();
		// double result=obj.classifyInstance(last);
		// double[] item = { 1, 1, 0, 1, 0, 0, 0 };
		// Instance newInstance = new DenseInstance(1, item);
		ArrayList<Integer> examples = new ArrayList<>();
		examples.add(0);
		examples.add(1);
		examples.add(1);
		examples.add(1);
		examples.add(1);
		examples.add(1);
		examples.add(1);
		addInstance(examples);
		System.out.println("AAAA:::" + classMap.get(obj.classifyInstance(addInstance(examples))));
	}

	public static void getClassification(ArrayList<Integer> examples) throws Exception {
		addInstance(examples);
		System.out.println("AAAA" + classMap.get(obj.classifyInstance(addInstance(examples))));
	}

	public static WekaInstance addInstance(List<Integer> featureValues) {
		WekaInstances instances;
		instances = new WekaInstances(7);
		instances.setClassIndex(instances.numAttributes() - 1);
		WekaInstance instance = instances.newInstance();
		for (int i = 0; i < featureValues.size(); i++) {
			instance.setValue(i, featureValues.get(i));
		}
		return instance;
	}

}