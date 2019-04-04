
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import java.io.BufferedReader;
import java.io.FileReader;
import weka.core.Instances;
import weka.classifiers.Evaluation;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;


public class TreeLearning {
    
    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in); 
        System.out.println("Enter file path");
        String DataFile = "/Users/vpati/Intuit_project/ml/weka/MiningAccidentData/data/data.arff";//in.nextLine(); //C:\\testing.arff
        
       Scanner selectedScn = new Scanner(System.in); 
        System.out.println("Enter selected tecnique 1 for j 48, 2 for RandomTree");
        String selectedTecnique = "2";//in.nextLine(); //C:\\testing.arff
        
        Instances training_data = new Instances(new BufferedReader(
                new FileReader(DataFile)));
        training_data.setClassIndex(training_data.numAttributes() - 1);

        Instances testing_data = new Instances(new BufferedReader(
                new FileReader(DataFile)));
        testing_data.setClassIndex(training_data.numAttributes() - 1);

      if(selectedTecnique.compareTo("1") == 0)
      {
        J48 j48 = new J48();
        FilteredClassifier selectedClassifier = new FilteredClassifier();
        j48.setMinNumObj(1);
        j48.setUnpruned(true);
        selectedClassifier.setClassifier(j48);

        selectedClassifier.buildClassifier(training_data);

        Evaluation evalJ48 = new Evaluation(training_data);
        evalJ48.evaluateModel(selectedClassifier, testing_data);
      
        //System.out.println(eval.pctCorrect());
        System.out.println(evalJ48.toSummaryString("\n=== J48 Results ===\n", false));
        
        System.out.println(evalJ48.toClassDetailsString());
       
        System.out.println(evalJ48.toMatrixString());
         
        try{
        PrintWriter writer = new PrintWriter("J48_Results.txt", "UTF-8");
        writer.println(evalJ48.toSummaryString("\n=== Results ===\n", false));
        writer.println(evalJ48.toClassDetailsString());
        writer.println(evalJ48.toMatrixString());
        writer.close();
        } catch (IOException e) {
  
        } 
      }
      else if(selectedTecnique.compareTo("2") == 0)
      {
        Evaluation evalRndTree = new Evaluation(training_data);
         RandomTree rTree =new RandomTree();
          FilteredClassifier selectedClassifier = new FilteredClassifier();
         selectedClassifier.setClassifier(rTree);
         selectedClassifier.buildClassifier(training_data);
         evalRndTree.evaluateModel(selectedClassifier, testing_data);
         
         
                 //System.out.println(eval.pctCorrect());
        System.out.println(evalRndTree.toSummaryString("\n=== Random Tree Results ===\n", false));
        
        System.out.println(evalRndTree.toClassDetailsString());
       
        System.out.println(evalRndTree.toMatrixString());
         
        try{
        PrintWriter writer = new PrintWriter("RandomTree_Results.txt", "UTF-8");
        writer.println(evalRndTree.toSummaryString("\n=== Results ===\n", false));
        writer.println(evalRndTree.toClassDetailsString());
        writer.println(evalRndTree.toMatrixString());
        writer.close();
        } catch (IOException e) {
  
        } 
      }
      else
      {
          System.out.println("Unknown input");
      }
    }
}