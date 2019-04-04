import org.apache.commons.math3.util.Precision;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.*;
import java.util.Map.Entry;

public class Weka {

    private static final List<String> popularSites = Arrays.asList(
            "/shuttle/missions/missions.html",
            "/shuttle/missions/sts-70/images/images.html",
            "/shuttle/missions/sts-71/images/images.html",
            "/shuttle/missions/sts-70/mission-sts-70.html",
            "/htbin/cdt_clock.pl, /software/winvn/winvn.html",
            "/shuttle/resources/orbiters/endeavour.html",
            "/shuttle/technology/sts-newsref/stsref-toc.html",
            "/facilities/lc39a.html",
            "/shuttle/countdown/countdown.html",
            "/history/apollo/apollo.html",
            "/elv/elvpage.htm",
            "/shuttle/missions/sts-69/mission-sts-69.html",
            "/htbin/cdt_main.pl",
            "/images/",
            "/history/history.html",
            "/shuttle/missions/sts-70/movies/movies.html",
            "/ksc.html",
            "/",
            "/shuttle/countdown/",
            "/shuttle/missions/sts-71/movies/movies.html",
            "/shuttle/technology/sts-newsref/sts_asm.html",
            "/whats-new.html",
            "/htbin/wais.pl",
            "/history/apollo/apollo-11/apollo-11.html",
            "/shuttle/missions/sts-71/mission-sts-71.html",
            "/history/apollo/apollo-13/apollo-13.html",
            "/shuttle/countdown/liftoff.html"
    );

    public void clustering(String filename, int numberofclusters) throws Exception {

        Instances data = ConverterUtils.DataSource.read(filename);

        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setPreserveInstancesOrder(true);
        kMeans.setNumClusters(numberofclusters);
        kMeans.buildClusterer(data);

        int[] assignments = kMeans.getAssignments();
        int j = 0;

        System.out.println("Square error: " + kMeans.getSquaredError() + "\n");
        for (double cluster : kMeans.getClusterSizes()) {
            System.out.println("Size of cluster " + j++ + " is " + cluster);
        }

        System.out.println("\nCentroids:");

        LinkedHashMap<Double, List<Integer>> centroids = new LinkedHashMap<>();
        int clus = 0;
        for (Instance instance : kMeans.getClusterCentroids()) {
            List<Integer> isVisited = new ArrayList<>();
            for (int i = 1; i < instance.numValues(); ++i) {
                isVisited.add((int) instance.value(i));
            }
            System.out.println("Cluster " + clus++ + ": " + Precision.round(instance.value(0), 2) + "\t " + isVisited);
            centroids.put(instance.value(0), isVisited);
        }

        System.out.println();

        List<Integer> visitsForExtraUser = intGen(kMeans.getClusterCentroids().get(0).numValues());
        System.out.println("Extra user: " + visitsForExtraUser + "\n");
        Map<Integer, Double> jaccardMap = new LinkedHashMap<>();

        Iterator<Integer> newUserIterator;
        Iterator<Integer> userIterator;

        int l = 0;
        for (Entry<Double, List<Integer>> doubleListEntry : centroids.entrySet()) {
            double and = 0;
            double or = 0;

            newUserIterator = visitsForExtraUser.iterator();
            userIterator = doubleListEntry.getValue().iterator();
            while (newUserIterator.hasNext() && userIterator.hasNext()) {
                if (ANDgate(newUserIterator.next(), userIterator.next())) {
                    and++;
                }
            }

            newUserIterator = visitsForExtraUser.iterator();
            userIterator = doubleListEntry.getValue().iterator();
            while (newUserIterator.hasNext() && userIterator.hasNext()) {
                if (ORgate(newUserIterator.next(), userIterator.next())) {
                    or++;
                }
            }

            jaccardMap.put(l++, and / or);
        }

        System.out.println("Jaccard values:");
        for (Entry<Integer, Double> doubleDoubleEntry : jaccardMap.entrySet()) {
            System.out.println("Cluster " + doubleDoubleEntry.getKey() + ": " + doubleDoubleEntry.getValue());
        }

        Integer clusterForNewUser = Collections.max(jaccardMap.entrySet(), Entry.comparingByValue()).getKey();
        System.out.println("\nNew user has been assigned to cluster " + clusterForNewUser);

        Iterator<String> iterator;

        List<String> popularSitesForNewUser = new LinkedList<>();
        iterator = popularSites.iterator();
        for (Integer isVisited : visitsForExtraUser) {
            if (isVisited == 1 && iterator.hasNext()) {
                popularSitesForNewUser.add(iterator.next());
            }
        }

        List<Integer> finalCluster = (List<Integer>) getElementByIndex(centroids, clusterForNewUser);
        List<String> clusPopSites = new LinkedList<>();
        iterator = popularSites.iterator();
        for (int i = 1; i < finalCluster.size(); ++i) {
            if (finalCluster.get(i) == 1 && iterator.hasNext()) {
                clusPopSites.add(iterator.next());
            }
        }

        System.out.println("\nRecommended sites:");
        clusPopSites.removeAll(popularSitesForNewUser);
        clusPopSites.forEach(System.out::println);
    }

    private static Object getElementByIndex(LinkedHashMap map, int index) {
        return map.get((map.keySet().toArray())[index]);
    }

    private static boolean ANDgate(int i, int j) {
        return i == 1 && j == 1;
    }

    private static boolean ORgate(int i, int j) {
        return (i == 0 && j == 1) || (i == 1 && j == 0)
                || ANDgate(i, j);
    }

    protected static List<Integer> intGen(int size) {
        Integer[] arr = new Integer[size];
        Random r = new Random();
        for (int i = 0; i < size; ++i) {
            arr[i] = r.nextInt(2);
        }
        return Arrays.asList(arr);
    }
}