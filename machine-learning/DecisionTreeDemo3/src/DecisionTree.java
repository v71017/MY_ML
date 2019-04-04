import java.util.ArrayList;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.Attribute;

public class DecisionTree {
	private DecisionTreeNode root;
	private ArrayList<Attribute> attributes; //all attributes an instance have
	// constructor
	public DecisionTree(Instances train_set, int m) {
		int numOfAttributes = train_set.numAttributes();
		this.attributes = new ArrayList<Attribute>();
		ArrayList<Attribute> candidateAttributes = new ArrayList<Attribute>();
		for(int i = 0; i < numOfAttributes - 1; i++) {
			attributes.add(train_set.attribute(i));
			candidateAttributes.add(train_set.attribute(i));
		}
		// add class attribute
		attributes.add(train_set.attribute(numOfAttributes - 1));
		this.root = buildDecisionTree(train_set, candidateAttributes, null, attributeMaxWeight(train_set), m);
	}
	
	public void print() {
		printTreeHelper(root, null, 0, 0);
	}
	
	public String traverse(Instance instance) {
		DecisionTreeNode node = this.root;
		while (!node.terminal) {
			// nominal attribute
			if (node.attribute.isNominal()) {
				String answer = instance.stringValue(node.attribute);
				int index = node.attribute.indexOfValue(answer);
				node = node.children.get(index);
			}
			// numeric attribute
			else {
				double answer = instance.value(node.attribute);
				int index;
				//check the threshold of numeric attribute
				if (answer <= Double.parseDouble(node.children.get(0).parentAttributeValue)) {
					index = 0;
				}
				else {
					index = 1;
				}
				node = node.children.get(index);
			}
		}
		return node.classLabel;
	}
	
	private void printTreeHelper(DecisionTreeNode root, DecisionTreeNode parent, int depth, int branchIndex) {
		int index = 0;
		// the node is root 
		if (parent == null) {
			for (DecisionTreeNode child : root.children) {
				printTreeHelper(child, root, 0, index);
				index ++;
			}
			return;
		} 
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("|\t");
		}
		Attribute parentAttribute = parent.attribute;
		String parentAttributeName = parentAttribute.name();
		sb.append(parentAttributeName);
		// numeric attribute
		if (parentAttribute.isNumeric()) {
			if (branchIndex == 0) {
				sb.append(" <= ");
			}
			else {
				sb.append(" > ");
			}
			System.out.print(sb.toString());
			System.out.format("%f", Double.parseDouble(root.parentAttributeValue));
		}
		// nominal attribute
		else {
			sb.append(" = ");
			String parentAttributeValue = root.parentAttributeValue;
			sb.append(parentAttributeValue);
			System.out.print(sb.toString());
		}
		StringBuilder s = new StringBuilder();
		s.append(" [" + root.numOfInstances[0] + " " + root.numOfInstances[1] + "]");
		if (root.terminal) {
			s.append(": " + root.classLabel);
			System.out.println(s.toString());
		} 
		else {
			System.out.println(s.toString());
			for(DecisionTreeNode child: root.children) {
				printTreeHelper(child, root, depth+1, index);
				index ++;
			}
		}
	}
	
	// build a decision tree and return a root node.
	private DecisionTreeNode buildDecisionTree(Instances instances, ArrayList<Attribute> candidateAttributes, 
			String parentAttributeValue, String majorityOfParent, int m) {
		int[] cases = new int[instances.numClasses()];
		// stopping criteria: the number of examples that reached the leaf node is 0, 
		// we output the majority value of the parent examples
		if (instances.isEmpty()) {
			for (int i = 0; i < cases.length; i++) {
				cases[i] = 0;
			}
			return new DecisionTreeNode(majorityOfParent, null, parentAttributeValue,
					true, cases);
		}
		int indexOfMajorityVote = 0;
		AttributeStats classStats =  instances.attributeStats(instances.classIndex());
		cases = classStats.nominalCounts;
		for (int i = 0; i < cases.length; i++) {
			// stopping criteria: (i) all of the training instances reaching the 
			// node belong to the same class
			if (cases[i] == instances.size()) {
				return new DecisionTreeNode(instances.classAttribute().value(i), 
						null, parentAttributeValue, true, cases);
			}
			if (cases[i] > cases[indexOfMajorityVote]) {
				indexOfMajorityVote = i;
			}
		}
		// stopping criteria: (ii) there are fewer than m training instances reaching the node	
		if (instances.size() < m) {
			return new DecisionTreeNode(instances.classAttribute().value(indexOfMajorityVote), 
					null, parentAttributeValue, true, cases);
		}
		// Candidate splits
		ArrayList<Split> candidateSplits = candidateSplits(instances, candidateAttributes);	
		Split bestSplit = bestSplit(instances, candidateSplits);
		// stopping criteria: (iii) no feature has positive information gain
		if (bestSplit == null) {
			 return new DecisionTreeNode(instances.classAttribute().value(indexOfMajorityVote), 
				null, parentAttributeValue, true, cases);
		}
		// stopping criteria: (iv)there are no more remaining candidate splits at the node
		if (candidateSplits.isEmpty()) {
			return new DecisionTreeNode(instances.classAttribute().value(indexOfMajorityVote), 
				null, parentAttributeValue, true, cases);
		}
		// stopping criteria: (iv)there are no more remaining candidate splits at the node
		if (candidateAttributes.isEmpty()) {
			return new DecisionTreeNode(instances.classAttribute().value(indexOfMajorityVote), 
				null, parentAttributeValue, true, cases);
		}
		// Create internal node. Store majorityVote in the label parameter.
		DecisionTreeNode root = new DecisionTreeNode(instances.classAttribute().value(indexOfMajorityVote),
				bestSplit.attribute, parentAttributeValue, false, cases);
		if (bestSplit.attribute.isNumeric()) {
			for (int i = 0; i < 2; i++) {
				// left branch <=
				if (i == 0) {
					root.addChild(buildDecisionTree(subInstances(bestSplit, i, instances),
							candidateAttributes, Double.toString(bestSplit.threshold), 
							instances.classAttribute().value(indexOfMajorityVote), m));
				}
				// right branch >
				if (i == 1) {
					root.addChild(buildDecisionTree(subInstances(bestSplit, i, instances),
						candidateAttributes, Double.toString(bestSplit.threshold), 
						instances.classAttribute().value(indexOfMajorityVote), m));
				}
			}
		}
		// bestSplit attribute is nominal
		else {
			ArrayList<Attribute> subCandidateAttributes = new ArrayList<Attribute>();
			// form a subAttributes list without bestAttribute
			for (Attribute attribute : candidateAttributes) {
				if (!attribute.equals(bestSplit.attribute)) {
					subCandidateAttributes.add(attribute);
				}
			}			
			for (int i = 0; i < bestSplit.attribute.numValues(); i++) {
				root.addChild(buildDecisionTree(subInstances(bestSplit, i, instances),
					subCandidateAttributes, bestSplit.attribute.value(i), 
					instances.classAttribute().value(indexOfMajorityVote), m));
			}
		}
		return root;
	}

	// return the value of an attribute that appears most
	private String attributeMaxWeight(Instances instances) {
		AttributeStats stats =  instances.attributeStats(instances.classIndex());
		int[] cases = stats.nominalCounts;
		int maxIndex = 0;
		for (int i = 0; i < cases.length; i++) {
			if (cases[i] > cases[maxIndex]) {
				maxIndex = i;
			}
		}
		return instances.classAttribute().value(maxIndex);
	}
	
	// Determine candidate splits for the input instances	 
	private ArrayList<Split> candidateSplits(Instances instances, ArrayList<Attribute> candidateAttributes) {
		ArrayList<Split> candidateSplits = new ArrayList<Split>();
		for (int i = 0; i < candidateAttributes.size(); i++){
			Attribute candidate = candidateAttributes.get(i);
			if(candidate.isNominal()) {
				candidateSplits.add(new Split(candidate));
			}
			else {
				ArrayList<Double> thresholds = candidateNumericSplits(instances, candidate);
				for (int j = 0; j < thresholds.size(); j++) {
					Double threshold = thresholds.get(j);
					candidateSplits.add(new Split(candidate, threshold));
				}
			}
		}
		return candidateSplits;
	}
	
	// Find the best split of input instances
	private Split bestSplit(Instances instances, ArrayList<Split> candidateSplits) {
		double bestGain = -10;
		Split bestSplit = null;
		double rootEntropy;
		AttributeStats labelStats = instances.attributeStats(instances.classIndex());
		int[] labelCounts = labelStats.nominalCounts;
		rootEntropy = calculateEntropy(labelCounts);
		for (int i = 0; i < candidateSplits.size(); i++) {
			Split split = candidateSplits.get(i);
			double gain = calculateGain(split, instances, rootEntropy);
			//when there is a tie exists
			if (gain > bestGain) {
				bestGain = gain;
				bestSplit = split;
			}	
		}
		// no feature has positive information gain
		if (bestGain <= 0) {
			return null;
		}
		return bestSplit;
	}
	
	//Determine list of threshold for candidate to split for numeric features
	private ArrayList<Double> candidateNumericSplits(Instances instances, Attribute attribute) {
		ArrayList<Double> thresholds = new ArrayList<Double>();
		ArrayList<ArrayList<Instance>> sets = new ArrayList<ArrayList<Instance>>();
		instances.sort(attribute);
		ArrayList<Instance> initialSet = new ArrayList<Instance>();
		initialSet.add(instances.get(0));
		Double value = instances.get(0).value(attribute);
		sets.add(initialSet);
		// divide instances into different groups
		for(int i = 1; i < instances.numInstances(); i++) {
			Instance instance = instances.get(i);
			if(instance.value(attribute) == value) {
				sets.get(sets.size() - 1).add(instance);
			}
			else {
				ArrayList<Instance> set = new ArrayList<Instance>();
				set.add(instance);
				sets.add(set);
				value = instance.value(attribute);
			}
		}
		// determine whether to add midpoints between each pair of group
		for(int i = 0; i < sets.size() - 1; i++) {
			boolean flag = false;
			String label = sets.get(i).get(0).stringValue(instances.classIndex());			
			for(int j = 1; j < sets.get(i).size(); j++) {
				if (!sets.get(i).get(j).stringValue(instances.classIndex()).equals(label)) {
					thresholds.add((sets.get(i).get(0).value(attribute) + 
							sets.get(i + 1).get(0).value(attribute)) / 2 );
					flag = true;
					break;
				}
			}
			if (flag == true) {
				continue;
			}
			for(int j = 0; j < sets.get(i + 1).size(); j++) {
				if (!sets.get(i + 1).get(j).stringValue(instances.classIndex()).equals(label)) {
					thresholds.add((sets.get(i).get(0).value(attribute) + 
							sets.get(i + 1).get(0).value(attribute)) / 2 );
					break;
				}
			}
		}
		return thresholds;
	}
		
	//sub instances according to the split
	private Instances subInstances (Split split, int i, Instances instances) {
		Instances subInstances = new Instances(instances.relationName(), this.attributes, instances.size());
		subInstances.setClassIndex(instances.classIndex());
		if (split.attribute.isNumeric()) {
			// corresponding to "<="
			if(i == 0) {
				for (int j = 0; j < instances.numInstances(); j++) {
					Instance instance = instances.get(j);
					if (instance.value(split.attribute) <= split.threshold) {
						subInstances.add(instance);
					}
				}
			}
			// corresponding to ">"
			else {
				for (int j = 0; j < instances.numInstances(); j++) {
					Instance instance = instances.instance(j);
					if (instance.value(split.attribute) > split.threshold) {
						subInstances.add(instance);
					}
				}
			}
		}
		// nominal attributes 
		else {
			for (int j = 0; j < instances.numInstances(); j++) {
				Instance instance = instances.instance(j);
				if (instance.stringValue(split.attribute).equals(split.attribute.value(i))) {
					subInstances.add(instance);
				}
			}
		}
		return subInstances;
	}
	
	// calculate gain of split
	private double calculateGain(Split s, Instances instances, double rootEntropy) {	
		double splitEntropy = 0;
		// nominal attribute
		if (s.attribute.isNominal()) {
			for (int i = 0; i < s.attribute.numValues(); i++) {
				int[] cases = new int[instances.numClasses()];
				double count = 0;
				for (int j = 0; j < instances.numInstances(); j++) {
					Instance instance = instances.instance(j);
					if (instance.stringValue(s.attribute).equals(s.attribute.value(i))) {
						String instanceClassAttribute = instance.stringValue(instances.classAttribute());
						cases[instances.classAttribute().indexOfValue(instanceClassAttribute)] ++;
						count ++;
					}
				}
				splitEntropy = splitEntropy + count / instances.size() * calculateEntropy(cases);
			}			
		}
		// numeric attribute
		else {
			for (int i = 0; i < 2; i++) {
				int[] cases = new int[instances.numClasses()];
				double count = 0;
				for (Instance instance : instances) {
					// corresponding to "<="
					if (i == 0) {
						if (instance.value(s.attribute) <= s.threshold) {
							String instanceClassAttribute = instance.stringValue(instances.classAttribute());
							cases[instances.classAttribute().indexOfValue(instanceClassAttribute)] ++;
							count ++;
						}
					}
					// corresponding to ">"
					else {
						if (instance.value(s.attribute) > s.threshold) {
							cases[instances.classAttribute().indexOfValue(
									instance.stringValue(instances.classAttribute()) )] ++;
							count ++;
						}
					}
				}
				splitEntropy = splitEntropy +  count / instances.size() * calculateEntropy(cases);
			}	
		}
		return rootEntropy - splitEntropy;
	}
	
	// calculate entropy of an attribute
	private double calculateEntropy(int[] cases) {
		double total = 0;
		double result = 0;
		for (int i = 0; i < cases.length; i++) {
			total = total + (double) cases[i];
		}
		if (total == 0) {
			return 0;
		}
		for (int i = 0; i < cases.length; i++) {
			if (cases[i] > 0) {
				result = result + (-1) * (cases[i] / total) * (Math.log(cases[i] / total) / Math.log(2));
			}
		}
		return result;
	}	
}