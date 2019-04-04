package tud.ke.ml.project.classifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.hamcrest.core.IsInstanceOf;

import tud.ke.ml.project.framework.classifier.ANearestNeighbor;
import tud.ke.ml.project.util.Pair;

/**
 * This implementation assumes the class attribute is always available (but
 * probably not set)
 * 
 * @author cwirth
 *
 */
public class NearestNeighbor extends ANearestNeighbor implements Serializable {

	// private static final long serialVersionUID = 2010906213520172559L;

	protected double[] scaling;
	protected double[] translation;
	List<List<Object>> trainingsData;
	List<Pair<Object, Integer>> majorityList;

	@Deprecated
	protected String[] getMatrikelNumbers() {
		return new String[] { getGroupNumber() };
	}

	@Override
	public String getGroupNumber() {
		return "43";
	}

	@Override
	protected void learnModel(List<List<Object>> data) {
		this.trainingsData = data;

		if (isNormalizing()) {
			double[][] normalizationArray = normalizationScaling();
			scaling = normalizationArray[1];
			translation = normalizationArray[0];

			Iterator<List<Object>> it = trainingsData.iterator();
			while (it.hasNext()) {
				List<Object> neighbour = it.next();
				for (int i = 0; i < neighbour.size(); i++) {
					Object attribute = neighbour.get(i);
					if (attribute instanceof Double) {
						double vNormalized = ((double) attribute - translation[i]) / scaling[i];
						neighbour.set(i, vNormalized);
					}
				}
			}
		}

		this.majorityList = new ArrayList<Pair<Object, Integer>>();
		HashMap<Object, Integer> majorityMap = new HashMap<Object, Integer>();
		Iterator<List<Object>> it = trainingsData.iterator();
		while (it.hasNext()) {
			List<Object> instance = it.next();
			Object classAttribute = instance.get(getClassAttribute());
			if (majorityMap.containsKey(classAttribute)) {
				Integer count = majorityMap.get(classAttribute);
				majorityMap.put(classAttribute, count + 1);
			} else {
				majorityMap.put(classAttribute, 1);
			}
		}

		Comparator<Pair<Object, Integer>> cd = new Comparator<Pair<Object, Integer>>() {
			@Override
			public int compare(Pair<Object, Integer> o1, Pair<Object, Integer> o2) {
				return -Integer.compare(o1.getB(), o2.getB());
			}
		};

		Set<Object> keys = majorityMap.keySet();
		Iterator<Object> keyIterator = keys.iterator();
		while (keyIterator.hasNext()) {
			Object key = keyIterator.next();
			Integer count = majorityMap.get(key);
			majorityList.add(new Pair<Object, Integer>(key, count));
		}

		majorityList.sort(cd);

	}

	protected Map<Object, Double> getUnweightedVotes(List<Pair<List<Object>, Double>> subset) {
		Map<Object, Double> resultMap = new HashMap<Object, Double>();
		Iterator<Pair<List<Object>, Double>> subsetIterator = subset.iterator();
		while (subsetIterator.hasNext()) {
			Pair<List<Object>, Double> instanceDistancePair = subsetIterator.next();
			Object classAttributeValue = instanceDistancePair.getA().get(getClassAttribute());
			if (resultMap.containsKey(classAttributeValue)) {
				double votes = resultMap.get(classAttributeValue);
				resultMap.put(classAttributeValue, votes + 1.0);
			} else {
				resultMap.put(classAttributeValue, 1.0);
			}
		}
		return resultMap;
	}

	protected Map<Object, Double> getWeightedVotes(List<Pair<List<Object>, Double>> subset) {
		Map<Object, Double> resultMap = new HashMap<Object, Double>();
		Iterator<Pair<List<Object>, Double>> subsetIterator = subset.iterator();
		while (subsetIterator.hasNext()) {
			Pair<List<Object>, Double> instanceDistancePair = subsetIterator.next();
			Object classAttributeValue = instanceDistancePair.getA().get(getClassAttribute());
			Double distanceValue = instanceDistancePair.getB();
			double weight = 1 / (distanceValue + 0.001);
			if (resultMap.containsKey(classAttributeValue)) {
				double votes = resultMap.get(classAttributeValue);
				resultMap.put(classAttributeValue, votes + weight);
			} else {
				resultMap.put(classAttributeValue, weight);
			}
		}
		return resultMap;
	}

	protected Object getWinner(Map<Object, Double> votes) {
		Object predictedClass = null;
		double highestVote = 0.0;
		Set<Object> keys = votes.keySet();
		Iterator<Object> votesIterator = keys.iterator();
		while (votesIterator.hasNext()) {
			Object key = votesIterator.next();
			double vote = votes.get(key);
			if (vote > highestVote) {
				predictedClass = key;
				highestVote = vote;
			}
		}

		List<Object> winners = new ArrayList<Object>();
		votesIterator = keys.iterator();
		while (votesIterator.hasNext()) {
			Object key = votesIterator.next();
			double vote = votes.get(key);
			if (vote == highestVote) {
				winners.add(key);
			}
		}

		Iterator<Pair<Object, Integer>> mlit = majorityList.iterator();
		while (mlit.hasNext()) {
			Pair<Object, Integer> classCount = mlit.next();
			if (winners.contains(classCount.getA())) {
				predictedClass = classCount.getA();
				break;
			}
		}
		return predictedClass;
	}

	protected Object vote(List<Pair<List<Object>, Double>> subset) {
		Map<Object, Double> votes;
		if (isInverseWeighting()) {
			votes = getWeightedVotes(subset);
		} else {
			votes = getUnweightedVotes(subset);
		}
		return getWinner(votes);
	}

	protected List<Pair<List<Object>, Double>> getNearest(List<Object> data) {
		List<Pair<List<Object>, Double>> distances = new LinkedList<Pair<List<Object>, Double>>();

		List<Object> ndata = data;

		if (isNormalizing()) {
			for (int i = 0; i < data.size(); i++) {
				Object attribute = data.get(i);
				if (attribute instanceof Double) {
					double vNormalized = ((double) attribute - translation[i]) / scaling[i];
					ndata.set(i, vNormalized);
				}
			}
		}

		Iterator<List<Object>> it = trainingsData.iterator();
		while (it.hasNext()) {
			List<Object> neighbour = it.next();

			Pair<List<Object>, Double> neighbourPair;
			if (getMetric() == 1) {
				neighbourPair = new Pair<List<Object>, Double>(neighbour, determineEuclideanDistance(ndata, neighbour));
			} else {
				neighbourPair = new Pair<List<Object>, Double>(neighbour, determineManhattanDistance(ndata, neighbour));
			}
			distances.add(neighbourPair);
		}

		int k = getkNearest();
		Comparator<Pair<List<Object>, Double>> cd = new Comparator<Pair<List<Object>, Double>>() {
			@Override
			public int compare(Pair<List<Object>, Double> o1, Pair<List<Object>, Double> o2) {
				return Double.compare(o1.getB(), o2.getB());
			}
		};
		
		distances.sort(cd);
		int size = distances.size();
		
		List<Pair<List<Object>, Double>> kNeighbours = new LinkedList<Pair<List<Object>, Double>>();
		for (int i = 0; i < distances.size(); i++) {
			Pair<List<Object>, Double> neighbour = distances.get(i);

			if (k > 0) {
				kNeighbours.add(neighbour);
			} else {
				Pair<List<Object>, Double> lastNeighbour = distances.get(i - 1);
				if (Double.compare(neighbour.getB(),lastNeighbour.getB()) == 0) {
					kNeighbours.add(neighbour);
				} else {
					break;
				}
			}
			k--;
		}

		return kNeighbours;
	}

	protected double determineManhattanDistance(List<Object> instance1, List<Object> instance2) {
		Double distance = 0.0;
		int indexClassAtttribute = getClassAttribute();

		for (int i = 0; i < instance1.size(); i++) {
			if (i != indexClassAtttribute) {
				Object attributeValue1 = instance1.get(i);
				Object attributeValue2 = instance2.get(i);
				if (attributeValue1 instanceof Double) {
					distance += Math.abs((Double) attributeValue1 - (Double) attributeValue2);
				} else if (attributeValue1 instanceof String) {
					distance += attributeValue1.equals(attributeValue2) ? 0.0 : 1.0;
				}
			}
		}

		return distance;
	}

	protected double determineEuclideanDistance(List<Object> instance1, List<Object> instance2) {
		Double distance = 0.0;
		int indexClassAtttribute = getClassAttribute();

		for (int i = 0; i < instance1.size(); i++) {
			if (i != indexClassAtttribute) {
				Object attributeValue1 = instance1.get(i);
				Object attributeValue2 = instance2.get(i);
				if (attributeValue1 instanceof Double) {
					distance += Math.pow(((Double) attributeValue1 - (Double) attributeValue2), 2);
				} else if (attributeValue1 instanceof String) {
					distance += attributeValue1.equals(attributeValue2) ? 0.0 : 1.0;
				}
			}
		}
		distance = Math.sqrt(distance);

		return distance;
	}

	protected double[][] normalizationScaling() {
		double[][] normalizationArray = new double[2][trainingsData.get(0).size()];

		for (int i = 0; i < normalizationArray[0].length; i++) {
			double minAttribute = Double.POSITIVE_INFINITY;
			double maxAttribute = Double.NEGATIVE_INFINITY;

			for (int j = 0; j < this.trainingsData.size(); j++) {
				Object attribute = this.trainingsData.get(j).get(i);
				if (attribute instanceof Double) {
					if ((double) attribute < minAttribute) {
						minAttribute = (double) attribute;
					}
					if ((double) attribute > maxAttribute) {
						maxAttribute = (double) attribute;
					}
				} else if (attribute instanceof String) {
					minAttribute = 0.0;
					maxAttribute = 1.0;
				}
			}

			normalizationArray[0][i] = minAttribute;
			normalizationArray[1][i] = maxAttribute - minAttribute;
			if (normalizationArray[1][i] == 0.0) {
				normalizationArray[1][i] = 1.0;
			}
		}
		return normalizationArray;
	}
}
