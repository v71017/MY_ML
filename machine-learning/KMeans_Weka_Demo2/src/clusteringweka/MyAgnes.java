/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clusteringweka;

import java.util.ArrayList;
import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Gerry
 */
public class MyAgnes extends AbstractClusterer {
    private Instances instances;
    private final int numClusters;
    private final String calculateLinkType;
    private final ArrayList<ArrayList<Instance>> clusters;
    private final ArrayList<ArrayList<Double>> clusterDistances;
    private final ArrayList<ArrayList<Integer>> numOfClusters;
    
    protected DistanceFunction distanceFunction = new EuclideanDistance();
    
    public MyAgnes(int numClusters, String calculateLinkType){
        this.numClusters = numClusters;
        this.calculateLinkType = calculateLinkType;
        clusters = new ArrayList<>();
        clusterDistances = new ArrayList<>();
        numOfClusters = new ArrayList<>();
    }
    
    public DistanceFunction getDistanceFunction(){
        return this.distanceFunction;
    }
    
    public void setDistanceFunciton(DistanceFunction distanceFunction){
        this.distanceFunction = distanceFunction;
    }
        
    @Override
    public void buildClusterer(Instances instances) throws Exception {
        for (int i = 0; i < instances.numInstances(); i++) {
            ArrayList<Instance> instance = new ArrayList<>();
            instance.add(instances.instance(i));
            clusters.add(instance);
        }
        
        distanceFunction.setInstances(instances);
        for (int i = 0; i < clusters.size(); i++) {
            ArrayList<Double> distances = new ArrayList<>();
            for (int j = 0; j < clusters.size(); j++) {
                Double distance = distanceFunction.distance(instances.instance(i), instances.instance(j));
                distances.add(distance);
            }
            clusterDistances.add(distances);
        }
        
        while (clusters.size() > numberOfClusters()) {
            int c1 = -1;
            int c2 = -1;
            double min = Double.MAX_VALUE;
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i+1; j < clusters.size(); j++) {
                    if (clusterDistances.get(i).get(j) <= min) {
                        min = clusterDistances.get(i).get(j);
                        c1 = i;
                        c2 = j;
                    }
                }
            }
            merge(c1,c2);
        }
        
        System.out.println("Cluster distances : " + clusterDistances);
    }

    public void merge(int c1, int c2) {
        String temp;
        clusters.get(c1).addAll(clusters.get(c2));
        clusters.remove(c2);        
        
        for (int i = 0; i < clusterDistances.get(c1).size(); i++) {
            if (this.calculateLinkType.equals("singlelinkage")) {
                if (clusterDistances.get(c1).get(i) > clusterDistances.get(c2).get(i)) {
                   clusterDistances.get(c1).set(i, clusterDistances.get(c2).get(i));
                }
            } else if (this.calculateLinkType.equals("completelinkage")) {
               if (clusterDistances.get(c1).get(i) < clusterDistances.get(c2).get(i)) {
                   clusterDistances.get(c1).set(i, clusterDistances.get(c2).get(i));
               }
            }
        }
        
        for (int i = 0; i < clusterDistances.size(); i++) {
            clusterDistances.get(i).remove(c2);
        }
        clusterDistances.remove(c2);
        System.out.println(this.toString());
    }
    
    @Override
    public String toString() {
        String temp = "";
        for (int i = 0; i < clusters.size(); i++) {
            temp += "Cluster " + i + " : " + clusters.get(i).size() + " instance(s) \n";
            temp += "Instances : \n";
            for (int j = 0; j < clusters.get(i).size(); j++) {
                temp += "" + (j + 1) + ". " + clusters.get(i).get(j) + "\n";
            }
            temp += " \n";
        }
        return temp;
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numClusters;
    }
}
