package com.technobium;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello Mahout world!
 * 
 */
public class BasicRecommender {

    public static void main(String[] args) throws IOException, TasteException {

        Logger log = LoggerFactory.getLogger(BasicRecommender.class);

        // Load historical data about user preferences
        DataModel model = new FileDataModel(new File("input/data.csv"));

        // Compute the similarity between users, according to their preferences
        UserSimilarity similarity = new EuclideanDistanceSimilarity(model);

        // Group the users with similar preferences
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1,
                similarity, model);

        // Create a recommender
        UserBasedRecommender recommender = new GenericUserBasedRecommender(
                model, neighborhood, similarity);

        // For the user with the id 1 get two recommendations
        List<RecommendedItem> recommendations = recommender.recommend(1, 2);
        for (RecommendedItem recommendation : recommendations) {
            log.info("User 1 might like the book with ID: "
                    + recommendation.getItemID() + " (predicted preference :"
                    + recommendation.getValue() + ")");
        }
    }
}
