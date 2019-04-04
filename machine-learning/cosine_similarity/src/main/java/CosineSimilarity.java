package main.java;
/**
 * Created by dewadkar on 8/12/2016.
 */
public class CosineSimilarity {


    public double cosineSimilarity(Object vectors, Object vectors2) {
        double[] docVector1 = (double[]) vectors;
        double[] docVector2 = (double[]) vectors2;
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (int i = 0; i < docVector1.length; i++)
        {
            dotProduct +=  docVector1[i] * docVector2[i];
            magnitude1 += Math.pow(docVector1[i], 2);
            magnitude2 += Math.pow(docVector2[i], 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            return  dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
    }
}