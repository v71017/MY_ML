import java.util.LinkedHashMap;

/**
 * @author ambar
 * This is the class holding the Decision Tree Node
 */
public class Node {
	
	private LinkedHashMap<String, LinkedHashMap<Integer, Integer>> set;
	private LinkedHashMap<String, LinkedHashMap<Integer, Integer>> zeroSet;
	private LinkedHashMap<String, LinkedHashMap<Integer, Integer>> oneSet;
	
	private double entropy;
	private String label;
	private boolean isLeaf;
	// ZeroSet is left child
	Node leftchild;
	// oneSet is right child
	Node rightChild;
	Node parent;
	private int decision;
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return
	 */
	public LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getZeroSet() {
		return zeroSet;
	}

	/**
	 * @param zeroSet
	 */
	public void setZeroSet(LinkedHashMap<String, LinkedHashMap<Integer, Integer>> zeroSet) {
		this.zeroSet = zeroSet;
	}

	/**
	 * @return
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @param parent
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * @return
	 */
	public LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getOneSet() {
		return oneSet;
	}

	/**
	 * @param oneSet
	 */
	public void setOneSet(LinkedHashMap<String, LinkedHashMap<Integer, Integer>> oneSet) {
		this.oneSet = oneSet;
	}

	/**
	 * @return
	 */
	public int getDecision() {
		return decision;
	}

	/**
	 * @param decision
	 */
	public void setDecision(int decision) {
		this.decision = decision;
	}

	/**
	 * @return
	 */
	public LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getSet() {
		return set;
	}

	/**
	 * @param set
	 */
	public void setSet(LinkedHashMap<String, LinkedHashMap<Integer, Integer>> set) {
		this.set = set;
	}

	/**
	 * @return
	 */
	public double getEntropy() {
		return entropy;
	}

	/**
	 * @param entropy
	 */
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	/**
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * @param isLeaf
	 */
	public void setLeaf(boolean isLeaf) {
		this.leftchild = null;
		this.rightChild = null;
		this.isLeaf = isLeaf;
	}

	/**
	 * @return
	 */
	public Node getLeftchild() {
		return leftchild;
	}

	/**
	 * @param leftchild
	 */
	public void setLeftchild(Node leftchild) {
		this.leftchild = leftchild;
	}

	/**
	 * @return
	 */
	public Node getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild
	 */
	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

}
