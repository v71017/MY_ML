
## REPORT
1.      Describe your program
            any programming tools or libraries that you used;
               Java, Gradle, Weka

            any other resources you used
                ---NA
                
## Answer the following questions based on your experimental results.
### Required: the top 30 features of each algorithm for each task.

1. Label prediction:  features for [ChiSquare,InfoGain and GainRatio       links](https://github.com/dewadkar/weka-textclassification/blob/master/resources/label/selectedAttributesLabelData.txt)

2. Product prediction:  features for [ChiSquare,InfoGain and GainRatio links](https://github.com/dewadkar/weka-textclassification/blob/master/resources/product/selectedAttributesProductData.txt)
                        

### Which feature selection method works best? What seems to affect the performance of each algorithm?
1. Label prediction using [Naive Bayes, J48, and SVM link](https://github.com/dewadkar/weka-textclassification/blob/master/resources/evaluation/label/classifierEvaluation.txt) 
            
2. Product prediction using [Naive Bayes, J48 and SVM link](https://github.com/dewadkar/weka-textclassification/blob/master/resources/evaluation/product/classifierEvaluation.txt)
            
     Observation: ChiSquare worked better in an avaragin the results of the experiment. Features are the most important element which affects the performance of prediction algorithm.  
     

### How many features are required for the different tasks, algorithms, and feature weight methods?
            -Number of feature for the task could not determined. 
            -Number of features for the algorithm evaluation, if less features mostly underfit and for more features it overfits.             - Methodlogy to determine the classifier features are unknown.

            

### Which text categorization algorithm is the most effective? Why it works better than other algorithms on this task?
           - Algorithms selected for the experiments are 1. J48-Decision tree 2. Naive Bayes 3. Support vector machine

               Observation: In both the task SVM outperforms in all attribute selection algorithm.
             Text similarity is observed in mulitdimension where each dimension contribute with integratation.
             Where as in NB assumtion is simple, each feature is independent. 
             Decision tree again evalutes on separable features, in most of the text prediction it is false. 
          
### What is missing in Report?

             As per assignment, Report of performance should be in terms of DodcID_Score_Method. This is not covered.
             The performance of the algorithm is measuerd by evaluating test set and not individual instance is analyzed.
             At this stage I am unware of the method to add new attribute (file name to the training and testing word
             vector format) which is obtained after StringToWordVector conversion. 
 
