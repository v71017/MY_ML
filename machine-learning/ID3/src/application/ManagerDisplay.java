package application;

import javax.swing.JFrame; 
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import tree.DecisionTree;


public class ManagerDisplay {

	public static void displayTree(DecisionTree _tree){
		MutableTreeNode root=_tree.getNode();
		final DefaultTreeModel model = new DefaultTreeModel(root);
	    final JTree tree = new JTree(model);
	    JFrame frame = new JFrame("ID3 Decision Tree");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(500, 500);
	    frame.getContentPane().add(new JScrollPane(tree));
	
	    frame.setVisible(true);
	}
}
