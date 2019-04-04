package com.machine.learning.decisiontrees;

import java.io.File;

import com.machine.learning.decisiontrees.DecisionTree;

public class Test {


	public static void main(String[] args) {
		
		DecisionTree tree = new DecisionTree();
		
		// Train your Decision Tree
		tree.train(new File("/Users/vpati/Intuit_project/ml/dt2/DecisionTree/DecisionTrees/resources/test.psv"));
		
		// Print RootNode display xml structure from your decision tree learning
		System.out.println(tree.getRootNode());

		// Classify your new data
		System.out.println(tree
				.classify("1|0|1|1|1|1|0|1"));

		
	}
	
	
}
