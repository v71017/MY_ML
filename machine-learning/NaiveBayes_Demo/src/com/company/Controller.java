package com.company;

public class Controller {
    private Learner learner;

    /**
     * Constructor of class controller
     */
    public Controller(){
        learner = new Learner();
        learner.loadDataset("src/data/spam_training.arff");
        learner.evaluate();
        learner.learn();
    }

    /**
     * Method to test a mesage as spam or not
     * @param message message to be tested
     * @return classification of the message
     */
    public int test(String message){
        SpamClassifier spamClassifier = new SpamClassifier(message);
        spamClassifier.makeInstance();
        return spamClassifier.classify(learner.getClassifier());
    }
}
