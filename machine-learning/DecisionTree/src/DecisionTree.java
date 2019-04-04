import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author ambar 
 * 			Logic for creating decision tree.
 */
public class DecisionTree {

	public List<String> attributeList;

	/**
	 * @param list
	 *            Constructor
	 */
	public DecisionTree(List<String> list) {
		this.attributeList = list;
	}

	/**
	 * @param set
	 */
	public Node createTree(LinkedHashMap<String, LinkedHashMap<Integer, Integer>> set, Node root) {
		root = createNode(set, root);
		// Terminating condition for recursion
		if (root.getEntropy() == 0) {
			root.setLeaf(true);
			if (root.getSet().get(attributeList.get(attributeList.size() - 1)).values().stream().filter(l -> l == 0)
					.count() > 0) {
				root.setDecision(0);
			} else {
				root.setDecision(1);
			}
		} else {
			setTheBestAttribute(root);

			if (root.getZeroSet() != null) {
				root.leftchild = new Node();
				root.leftchild.setParent(root);
				root.leftchild = createTree(root.getZeroSet(), root.leftchild);
			} else {
				root.setLeaf(true);
			}
			if (root.getOneSet() != null) {
				root.rightChild = new Node();
				root.rightChild.setParent(root);
				root.rightChild = createTree(root.getOneSet(), root.rightChild);
			} else {
				root.setLeaf(true);
			}
		}
		return root;
	}

	/**
	 * @param root
	 *            Selected the Attribute with max gain for the DT
	 */
	private void setTheBestAttribute(Node root) {
		double maxGain = Double.MIN_VALUE;
		double gain;
		String selectedAttribute = null;
		for (int i = 0; i < attributeList.size() - 1; i++) {
			String attribute = attributeList.get(i);

			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> zeroSet = getZeroSet(root.getSet(), attribute);
			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> oneSet = getOneSet(root.getSet(), attribute);

			gain = calculateGain(root, attribute, root.getSet(), zeroSet, oneSet);
			if (gain > maxGain) {
				maxGain = gain;
				selectedAttribute = attribute;
				root.setOneSet(oneSet);
				root.setZeroSet(zeroSet);
			}
		}
		root.setLabel(selectedAttribute);
	}

	/**
	 * @param root
	 * @param attribute
	 * @param set
	 * @param oneSet
	 * @param zeroSet
	 * @return Gain value
	 * 
	 */
	private double calculateGain(Node root, String attribute,
			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> set,
			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> zeroSet,
			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> oneSet) {
		double entropy;
		entropy = root.getEntropy();

		double countZero = set.get(attribute).values().stream().filter(l -> l == 0).count();

		double countOne = set.get(attribute).values().stream().filter(l -> l == 1).count();
		double total = countOne + countZero;

		double entropy1 = calculateEntropy(zeroSet.get(attributeList.get(attributeList.size() - 1)).values());
		double entropy2 = calculateEntropy(oneSet.get(attributeList.get(attributeList.size() - 1)).values());

		double gain = 0;
		// Gain computation in case of InformationGain and ImpurityVariance is
		// same. Multiple line just for clarification
		if (ProgramDT.ENTROPY) {
			gain = entropy - ((countZero / total) * entropy1) - ((countOne / total) * entropy2);
		} else {
			gain = entropy - ((countZero / total) * entropy1) - ((countOne / total) * entropy2);
		}
		return gain;
	}

	/**
	 * @param set
	 * @param attribute
	 * @return
	 */
	private LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getOneSet(
			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> set, String attribute) {

		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> oneSet = deepCopy(set);
		LinkedHashMap<Integer, Integer> tempSet = oneSet.get(attribute);
		ArrayList<Integer> removeKeys = new ArrayList<Integer>();

		for (Integer i : tempSet.keySet()) {
			if (tempSet.get(i) == 0) {
				removeKeys.add(i);
			}
		}

		for (int i = 0; i < attributeList.size(); i++) {
			for (Integer j : removeKeys) {
				oneSet.get(attributeList.get(i)).remove(j);
			}
		}
		return oneSet;
	}

	/**
	 * @param set
	 * @param attribute
	 * @return
	 */
	private LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getZeroSet(
			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> set, String attribute) {

		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> zeroSet = deepCopy(set);
		LinkedHashMap<Integer, Integer> temp = zeroSet.get(attribute);
		ArrayList<Integer> removeKeys = new ArrayList<Integer>();

		for (Integer i : temp.keySet()) {
			if (temp.get(i) == 1) {
				removeKeys.add(i);
			}
		}

		for (int i = 0; i < attributeList.size(); i++) {
			for (Integer j : removeKeys) {
				zeroSet.get(attributeList.get(i)).remove(j);
			}
		}
		return zeroSet;
	}

	/**
	 * @param set
	 * @param node
	 * @return
	 */
	private Node createNode(LinkedHashMap<String, LinkedHashMap<Integer, Integer>> set, Node node) {
		node.setSet(set);
		double entropy = calculateEntropy(set.get(attributeList.get(attributeList.size() - 1)).values());
		node.setEntropy(entropy);
		return node;
	}

	/**
	 * @param values
	 * @return Entropy
	 */
	private double calculateEntropy(Collection<Integer> values) {
		double entropy = 0;
		if (ProgramDT.ENTROPY) {
			double countZero = values.stream().filter(l -> l == 0).count();
			double countOne = values.stream().filter(l -> l == 1).count();
			double total = countOne + countZero;

			if ((countZero == total) || (countOne == total)) {
				return 0;
			} else {
				double probabilityZero = countZero / total;
				double probabilityOne = countOne / total;

				entropy = -probabilityZero * (Math.log10(probabilityZero) / Math.log10(2))
						- probabilityOne * (Math.log10(probabilityOne) / Math.log10(2));

			}
		} else {
			entropy = calculateVarianceImpurity(values);
		}
		return entropy;
	}

	/**
	 * @param values
	 * @return VarianceImppurity
	 */
	private double calculateVarianceImpurity(Collection<Integer> values) {
		double countZero = values.stream().filter(l -> l == 0).count();
		double countOne = values.stream().filter(l -> l == 1).count();
		double total = countOne + countZero;

		return (countZero / total) * (countOne / total);
	}

	/**
	 * @param original
	 * @return Deep copy object of data
	 * 
	 *         As copy is my default shallow copy, need to implement deep copy
	 *         for creating different dataset
	 * 
	 */
	public LinkedHashMap<String, LinkedHashMap<Integer, Integer>> deepCopy(
			LinkedHashMap<String, LinkedHashMap<Integer, Integer>> original) {

		if(original == null){
			return null;
		}
		LinkedHashMap<String, LinkedHashMap<Integer, Integer>> copy = new LinkedHashMap<String, LinkedHashMap<Integer, Integer>>();

		for (Entry<String, LinkedHashMap<Integer, Integer>> entry : original.entrySet()) {
			copy.put(entry.getKey(), new LinkedHashMap<Integer, Integer>(entry.getValue()));
		}
		return copy;
	}

	/**
	 * @param root
	 * @param data
	 * @return Accuracy in percentage
	 */
	public double calculateAccuracy(Node root, LinkedHashMap<String, LinkedHashMap<Integer, Integer>> data) {
		double accuracy = 0;
		double dataSize = data.get(attributeList.get(attributeList.size() - 1)).values().size();

		for (int i = 2; i < dataSize + 2; i++) {
			ArrayList<Integer> row = new ArrayList<Integer>();
			for (String key : data.keySet()) {
				row.add(new ArrayList<Integer>(data.get(key).values()).get(i - 2));
			}
			if (isPredicted(root, row)) {
				accuracy++;
			}
		}
		return (accuracy / dataSize * 100);
	}

	/**
	 * @param root
	 * @param row
	 * @return prediction status
	 */
	private boolean isPredicted(Node root, ArrayList<Integer> row) {
		if (root.isLeaf()) {
			int actualDec = row.get(attributeList.size() - 1);
			int predictedDec = root.getDecision();
			return actualDec == predictedDec;
		} else {
			int value = row.get(attributeList.indexOf(root.getLabel()));
			if (value == 1) {
				return isPredicted(root.rightChild, row);
			} else {
				return isPredicted(root.leftchild, row);
			}
		}
	}

	/**
	 * @param root
	 * @param depth
	 * @return Tree to be printed
	 */
	public String printTree(Node root, int depth) {
		StringBuilder result = new StringBuilder();
		// Print left side
		for (int i = 0; i < depth; i++) {
			result.append("| ");
		}
		result.append(root.getLabel() + " = 0 :");
		if(root.leftchild!=null){
		if (!root.leftchild.isLeaf())
			result.append("\n" + printTree(root.leftchild, depth + 1));
		else
			result.append(" " + root.leftchild.getDecision() + "\n");
		}
		// Print right side
		for (int i = 0; i < depth; i++) {
			result.append("| ");
		}
		result.append(root.getLabel() + " = 1 :");
		if(root.rightChild!=null){
		if (!root.rightChild.isLeaf())
			result.append("\n" + printTree(root.rightChild, depth + 1));
		else
			result.append(" " + root.rightChild.getDecision() + "\n");
		}
		return result.toString();
	}

	
	/**
	 * Pruning the tree based on given L and K value.
	 */
	public Node prune(int L, int K, Node root, LinkedHashMap<String, LinkedHashMap<Integer, Integer>> validationData) {
		Node best = new Node();
		best = copy(root);
		for (int i = 1; i < L; i++) {
			Node Dnew = new Node();
			Dnew = copy(root);

			int m = (int) (Math.random() * K + 1);
			for (int j = 1; j < m; j++) {
				int N = 0;
				N = countNonLeaf(Dnew, N);
				int P = (int) (Math.random() * N + 1);
				//Temp indicate the indexed node at P
				Node temp = null;
				temp = getSubset(P, Dnew, temp);

				// Calculating and assigning the majority class at P subtree
				long zeroCount = temp.getZeroSet().get(attributeList.get(attributeList.size() - 1)).values().stream()
						.filter(l -> l == 0).count();
				zeroCount += temp.getOneSet().get(attributeList.get(attributeList.size() - 1)).values().stream()
						.filter(l -> l == 0).count();
				long oneCount = temp.getZeroSet().get(attributeList.get(attributeList.size() - 1)).values().stream()
						.filter(l -> l == 1).count();
				oneCount += temp.getOneSet().get(attributeList.get(attributeList.size() - 1)).values().stream()
						.filter(l -> l == 1).count();
				temp.setDecision((int) (oneCount > zeroCount ? 1 : 0));
				//Pruning the tree at P.
				temp.setLeaf(true);
			}
			double accBest = calculateAccuracy(best, validationData);
			double accDnew = calculateAccuracy(Dnew, validationData);
			if (accDnew > accBest) {
				best = Dnew;
			}
		}
		return best;
	}
	
	/**
	 * @param p determines index of the node.
	 * @param root : root of the tree
	 * @return the node indicated by index P 
	 */
	private Node getSubset(int p, Node root, Node temp) {
		if(root.getIndex() == p) {
			temp=root;
			return temp;
		}
		else{
			if(root.leftchild!=null){
				temp = getSubset(p, root.leftchild,temp);
			}
			if(root.rightChild!=null){
				temp = getSubset(p, root.rightChild,temp);
			}
		}
		return temp;
	}

	/**
	 * This method counts and set the index of all the non-leaf nodes.
	 * @return Total number of non-leaf nodes.
	 */
	private int countNonLeaf(Node root, int count) {
		if(root!=null) {
			if(!root.isLeaf()) {
				count++;
				root.setIndex(count);
			}
			count = countNonLeaf(root.leftchild,count);
			count = countNonLeaf(root.rightChild,count);
		}
		return count;
	}

	/**
	 * Returns the copy of the tree.
	 */
	private Node copy(Node root) {
		Node copy=null;
		if (root!=null) {
			copy = new Node();
			copy.setDecision(root.getDecision());
			copy.setEntropy(root.getEntropy());
			copy.setLabel(root.getLabel());
			copy.setLeaf(root.isLeaf());
			// Do deep copy here

			copy.setSet(deepCopy(root.getSet()));
			copy.setZeroSet(deepCopy(root.getZeroSet()));
			copy.setOneSet(deepCopy(root.getOneSet()));

			copy.setLeftchild(copy(root.leftchild));
			copy.setRightChild(copy(root.rightChild));
		}
		return copy;
	}
}
