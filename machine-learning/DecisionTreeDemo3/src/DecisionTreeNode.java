import java.util.ArrayList;
import weka.core.Attribute;

public class DecisionTreeNode {
	public Attribute attribute;
	public String classLabel; 
	public String parentAttributeValue; // if is the root, set to "-1"
	public boolean terminal;
	public int[] numOfInstances;
	public ArrayList<DecisionTreeNode> children;

	public DecisionTreeNode(String label, Attribute attribute, String parentAttributeValue, 
			boolean terminal, int[] numOfInstances) {
		this.attribute = attribute;
		this.classLabel = label;
		this.parentAttributeValue = parentAttributeValue;
		this.terminal = terminal;
		//leaf has no children
		if (terminal == true) {
			this.children = null;
		} 
		else {
			this.children = new ArrayList<DecisionTreeNode>();
		}
		this.numOfInstances = numOfInstances;
	}

	//add a child to the list of children of the TreeNode
	public void addChild(DecisionTreeNode child) {
		if (children != null) {
			children.add(child);
		}
	}
}