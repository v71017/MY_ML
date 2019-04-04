package com.mavi.cagatay;

import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * Created by cyurtoz on 13/03/17.
 */
public class ClassifyNewInstance {
	public static void main(String[] args) throws Exception {

		final InputStream resourceAsStream =  new FileInputStream("/Users/vpati/Intuit_project/ml/rf/HR-Analytics/src/com/mavi/cagatay/HR_comma_sep.arff"); 

		Instances data = new Instances(new InputStreamReader(resourceAsStream));


		// discretize attributes
		Discretize discretize = new Discretize();
		discretize.setAttributeIndices("1,2,3,4,5");
		discretize.setBins(10);
		discretize.setInputFormat(data);
		final Instances instances = Filter.useFilter(data, discretize);

		instances.setClassIndex(6);


		//classify
		weka.classifiers.trees.RandomForest rf = new RandomForest();
		rf.setMaxDepth(10);
		rf.setNumTrees(30);

		rf.buildClassifier(instances);
		System.out.println(rf.toString());


		final InputStream in = System.in;
		Scanner sc = new Scanner(in);


		while (true) {
			System.out.print("\n Enter your comma separated input, ? for the the class(left) attribute: \n");
			System.out.print("\n satisfactionLvl,perf_score,number_project,avg_montly_hours, " +
					"years_spent, work_accident,left," +
					" promoted_5_years," +
					" dept,salary\n");

			String input = sc.nextLine();
			input = prepareArff(input);
			Instances instances1 = new Instances(new StringReader(input));
			final Instances instances1Filtered = Filter.useFilter(instances1, discretize);
			instances1Filtered.setClassIndex(6);


			if ("q".equals(input)) {
				System.out.println("Exit!");
				break;
			} else {
				final double v = rf.classifyInstance(instances1Filtered.instance(0));
				if (v == 0) {
					System.out.println("--> This Employee will STAY");

				} else {
					System.out.println("--> This Employee will LEAVE");

				}

			}

			System.out.println("-----------\n");
		}

		sc.close();

	}

	private static String prepareArff(String input) {
		return "@relation HR_comma_sep\n" +
				"\n" +
				"@attribute satisfaction_level numeric\n" +
				"@attribute last_evaluation numeric\n" +
				"@attribute number_project numeric\n" +
				"@attribute average_montly_hours numeric\n" +
				"@attribute time_spend_company numeric\n" +
				"@attribute Work_accident {0,1}\n" +
				"@attribute left {0,1}\n" +
				"@attribute promotion_last_5years {0,1}\n" +
				"@attribute sales {sales,accounting,hr,technical,support,management,IT,product_mng,marketing," +
				"RandD}\n" +
				"@attribute salary {low,medium,high}\n @data \n " + input;
	}

}
