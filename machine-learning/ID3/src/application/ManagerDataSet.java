package application;

import java.util.Enumeration;

import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;


public class ManagerDataSet {

	public static Instances discretize(Instances dataSet) throws Exception{
		Discretize disc = new Discretize();
		String[] options=new String[2];
		options[0]="-R";
		int index=1;
		Attribute a=dataSet.attribute(index);
		while(a!=dataSet.attribute(dataSet.classIndex())){
			if(a.isNumeric()){
				options[1]=String.valueOf(a.index());
				disc.setOptions(options);
				disc.setInputFormat(dataSet);
				dataSet= Filter.useFilter(dataSet, disc);
				
			}
			index++;
			a=dataSet.attribute(index);
		}
		return dataSet;
	}

public static String majorityElement(Instances dataSet,Attribute A){
		
		double[]occurrences=new double[dataSet.numDistinctValues(A)];
		String[]values=new String[dataSet.numDistinctValues(A)];
		Enumeration en=A.enumerateValues();
		int index=0;
		while(en.hasMoreElements()){
			String v=(String)en.nextElement();
			occurrences[index]=ManagerDataSet.occurrence_without_miss_val(dataSet, v, A);
			values[index]=v;
			index++;
		}
		int max=0;
		for(int i=0;i<dataSet.numDistinctValues(A);i++){
			if(occurrences[i]>occurrences[max])
				max=i;
		}
		return values[max];
		
		}

	//used only in majorityElement
	private static double occurrence_without_miss_val(Instances dataSet,String value,Attribute A){
		double occ=0;
		for(int i=0;i<dataSet.numInstances();i++){
			if(dataSet.instance(i).stringValue(A)==value)
				occ++;
		}	
		return occ;
	}
	
	public static double occurrence(Instances dataSet,String value,Attribute A){
		double occ=0;
		for(int i=0;i<dataSet.numInstances();i++){
			if(dataSet.instance(i).isMissing(A)&& majorityElement(dataSet,A)==value){
				occ++;
			}
			if(dataSet.instance(i).stringValue(A)==value)
				occ++;
		}	
		return occ;
	}
	
	public static Instances[] split(Instances dataSet) throws Exception{
		
		dataSet.randomize(new java.util.Random());
		int length=dataSet.numInstances();
		int threshold=(int) Math.ceil(length*7/10);
		Instances[] _split=new Instances[2];
		
		_split[0]=new Instances(dataSet,threshold);	
		for(int i=0;i<threshold;i++){
			_split[0].add(dataSet.instance(i));
		}
		_split[1]=new Instances(dataSet,dataSet.numInstances()-threshold);
		for(int i=threshold;i<dataSet.numInstances();i++){
			_split[1].add(dataSet.instance(i));
		}
		return _split;
	}
	
	public static boolean sameClass(Instances dataSet){
		String val=dataSet.firstInstance().stringValue(dataSet.classIndex());
		for(int i=1;i<dataSet.numInstances();i++){
			if(val!=dataSet.instance(i).stringValue(dataSet.classIndex()))
				return false;
			val=dataSet.instance(i).stringValue(dataSet.classIndex());
		}
		return true;
	}
}
