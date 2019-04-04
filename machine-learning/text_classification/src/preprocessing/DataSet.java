package preprocessing;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dewadkar on 8/5/2016.
 */
public class DataSet {
    public static String DATA_SET_TEST = "/Users/vpati/Intuit_project/ml/weka/weka-textclassification/resources/hw2/auto.dat.test";
    public static String DATA_SET_TRAIN = "/Users/vpati/Intuit_project/ml/weka/weka-textclassification/resources/hw2/auto.dat.train";
    public static String DATA_SET_TEST_FILES = "/Users/vpati/Intuit_project/ml/weka/weka-textclassification/resources/dataset/product/testing";
    public static String DATA_SET_TRAIN_FILES = "/Users/vpati/Intuit_project/ml/weka/weka-textclassification/resources/dataset/product/training";


    public File createFilesDataSetFromSingleFile(String raw_data_file_path, String dirPath) {
        List<String> documents = documents(raw_data_file_path);
        for (String document : documents) {
            try{
            createDocumentAsFile(document,dirPath );

            }catch (Exception e){
                System.out.println(" -- - ");
            }
        }

        return null;
    }

    private void createDocumentAsFile(String document, String dirPath) {
        String docID = documentField(document, "DOCID");
        String product = isCarFord(documentField(document, "PRODUCT")) ? "FORDCAR" : "OTHERCAR";
        String label = documentText(document, "label", "rating");
        String docName = docID.trim()+ "_" + product.trim() + "_" + label.trim()+".txt";
        System.out.println("doc_name "+docName);
        try {
//            FileUtils.writeStringToFile(new File(dirPath+"/"+label.trim()+"/"+docName.trim()), document, "UTF-8");
            FileUtils.writeStringToFile(new File(dirPath+"/"+product.trim()+"/"+docName.trim()), document, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isCarFord(String product) {
        return product.toLowerCase().contains("ford");
    }

    private String documentField(String document, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">(.+?)</" + tag + ">");
        Matcher matcher = pattern.matcher(document);
        matcher.find();
        return matcher.group(1);
    }

    private String documentText(String document, String startTag, String endTag) {
        String matchedText = document.substring(document.indexOf(startTag) + startTag.length()+1, document.indexOf(endTag)-1);
        return matchedText.trim();
    }


    private List<String> documents(String dataSetFilePath) {
        try {
            String content = FileUtils.readFileToString(new File(dataSetFilePath));
            List<String> tagValues = new ArrayList<String>();
            int i = 0;
            int temp = 0;
            while ((i = (content.indexOf("<DOC>", i) + 1)) > 0) {
                if (temp == 0) {
                    temp = i;
                } else {
                    tagValues.add(content.substring(temp, i));
                    temp = i;
                }
            }
            tagValues.add(content.substring(temp, content.length()));
            System.out.println(" Total Documents " + tagValues.size());
            return tagValues;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public static void main(String[] args) {
        DataSet dataSet = new DataSet();
//        dataSet.createFilesDataSetFromSingleFile(DATA_SET_TRAIN,LABEL_TRAIN_DATA_SET_DIR_PATH);
        dataSet.createFilesDataSetFromSingleFile(DATA_SET_TEST,DATA_SET_TEST_FILES);

    }

    public void createTrainFileToDataSetDirForCar() {
        createFilesDataSetFromSingleFile(DATA_SET_TRAIN,DATA_SET_TRAIN_FILES);
    }

    public void createTestFileToDataSetDirForCar() {
        createFilesDataSetFromSingleFile(DATA_SET_TEST,DATA_SET_TEST_FILES);
    }

    public void createTrainFileToDataSetDirForCarRecommendation() {
        createFilesDataSetFromSingleFile(DATA_SET_TRAIN,DATA_SET_TRAIN_FILES);

    }

    public void createTestFileToDataSetDirForCarRecommandation() {
        createFilesDataSetFromSingleFile(DATA_SET_TEST,DATA_SET_TEST_FILES);

    }
}
