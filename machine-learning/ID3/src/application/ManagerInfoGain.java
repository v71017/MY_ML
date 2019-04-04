package application;

import java.util.Enumeration;

import weka.core.*;

public class ManagerInfoGain {

	private static double calc_entropy(Instances dataSet){
		double entropy=0;
		Enumeration en=dataSet.classAttribute().enumerateValues();
		while(en.hasMoreElements()){
			String v=(String)en.nextElement();
			double occ=ManagerDataSet.occurrence(dataSet, v,dataSet.classAttribute()) ;
			if(occ!=0){			
				entropy+= (occ/dataSet.numInstances())*(Math.log10(dataSet.numInstances()/occ)/Math.log10(2));				
			}
		}
		return entropy;
	}
	public static double calc_infoGain(Instances dataSet, Attribute A){
		double infoGain=calc_entropy(dataSet);
		Enumeration en=A.enumerateValues();
		while(en.hasMoreElements()){
			String v=(String)en.nextElement();
			Instances v_dataSet=new Instances(dataSet,dataSet.numInstances());
			for(int i=0;i<dataSet.numInstances();i++){
				if(dataSet.instance(i).stringValue(A)==v)
					v_dataSet.add(dataSet.instance(i));
				if(dataSet.instance(i).isMissing(A)&&v==ManagerDataSet.majorityElement(dataSet, A))
					v_dataSet.add(dataSet.instance(i));
			}
			v_dataSet.compactify();
			infoGain-=ManagerDataSet.occurrence(dataSet, v, A)/dataSet.numInstances()*calc_entropy(v_dataSet);
		}
		return infoGain;
	}
}
