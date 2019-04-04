package application;

import java.util.Enumeration;



import tree.DecisionTree;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Id3 {

	public static DecisionTree decisionTreeLearner(Instances dataSet,Instances p_dataSet,String assegnation) throws Exception{
		
		if(dataSet.numInstances()==0){
			return new DecisionTree(ManagerDataSet.majorityElement(p_dataSet,p_dataSet.classAttribute()),assegnation);
		}
		if(ManagerDataSet.sameClass(dataSet)){
			return new DecisionTree(dataSet.firstInstance().stringValue(dataSet.classIndex()),assegnation);
		}
		if(dataSet.numAttributes()==1){
			return new DecisionTree(ManagerDataSet.majorityElement(dataSet,dataSet.classAttribute()),assegnation);
		}
		Attribute A=Id3.selectAttribute(dataSet);
		
		DecisionTree tree=new DecisionTree(A,assegnation);	
		
		Enumeration values =A.enumerateValues();
		
		while(values.hasMoreElements()){
			String v=(String) values.nextElement();
			Instances v_dataSet=new Instances(dataSet,dataSet.numInstances());		
			for(int i=0;i<dataSet.numInstances();i++){				
				if(dataSet.instance(i).stringValue(A)==v)
					v_dataSet.add(dataSet.instance(i));		
			}
			Remove rem=new Remove();
			int[]ind=new int[1];
			ind[0]=A.index();		
			rem.setAttributeIndicesArray(ind);
			rem.setInputFormat(v_dataSet);
			v_dataSet=Filter.useFilter(v_dataSet,rem);
		    tree.setChild(decisionTreeLearner(v_dataSet,dataSet,v),tree);
		}
			
		return tree;
			
		
	}
	
	private static Attribute selectAttribute(Instances dataSet) throws Exception{
		Attribute A=dataSet.attribute(0);
		double gain=ManagerInfoGain.calc_infoGain(dataSet, A);
		double max_gain=gain;
		int ind=0;
		for(int i=1;i<dataSet.numAttributes()-1;i++){
				A=dataSet.attribute(i);
				gain=ManagerInfoGain.calc_infoGain(dataSet, A);
				if(gain>max_gain){
					max_gain=gain;	
					ind=i;
				}
		}
		return dataSet.attribute(ind);
	}

		
	}

