package tree;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import weka.core.Attribute;

public class DecisionTree {

	private ArrayList<DecisionTree> childrens;
	private String attribute;
	private String edge;
	private String result;
	private MutableTreeNode node;

	public DecisionTree(Attribute attribute,String edge){
		childrens=new ArrayList<DecisionTree>();
		this.attribute=attribute.name();
		this.edge=edge;
		this.result=null;
		node = new DefaultMutableTreeNode("-"+attribute.name()+"-      edge: -"+edge+"-");
	
	}
	//leaf nodes
	public DecisionTree(String result,String edge){
		this.attribute=null;
		this.edge=edge;
		this.result=result;
		node = new DefaultMutableTreeNode("-"+result+"-      edge: -"+edge+"-");
	}
	
	public void setChild(DecisionTree child,DecisionTree parent){
		childrens.add(child);
		parent.getNode().insert(child.getNode(), 0);
		
	}
	public MutableTreeNode getNode() {
		return node;
	}
	public String getAttribute() {
		return attribute;
	}
	public String getEdge() {
		return edge;
	}
	public String getResult() {
		return result;
	}
	public ArrayList<DecisionTree> getChildrens() {
		return childrens;
	}
	
}
