package preprocessing;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by dewadkar on 8/7/2016.
 */
public class TextDirectoryToArff {

    public Instances createDataset(String directoryPath) throws Exception {

        TextDirectoryLoader textDirectoryLoader = new TextDirectoryLoader();
//        textDirectoryLoader.
        FastVector atts = new FastVector(2);
        atts.addElement(new Attribute("class", (FastVector) null));
        atts.addElement(new Attribute("contents", (FastVector) null));
        Instances data = new Instances("text_files_in_" + directoryPath, atts, 0);

        File dir = new File(directoryPath);
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".txt")) {
                try {
                    double[] newInst = new double[2];
                    newInst[0] = (double)data.attribute(0).addStringValue(files[i]);
                    File txt = new File(directoryPath + File.separator + files[i]);
                    InputStreamReader is;
                    is = new InputStreamReader(new FileInputStream(txt));
                    StringBuffer txtStr = new StringBuffer();
                    int c;
                    while ((c = is.read()) != -1) {
                        txtStr.append((char)c);
                    }
                    newInst[1] = (double)data.attribute(1).addStringValue(txtStr.toString());
                    data.add(new Instance(1.0, newInst));
                } catch (Exception e) {
                    //System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
                }
            }
        }
        return data;
    }

    static String DATA_SET_TEST_FILES = "C:\\Users\\IBM_ADMIN\\IdeaProjects\\nlp\\resources\\dataset\\testing";
    static String DATA_SET_TRAIN_FILES = "C:\\Users\\IBM_ADMIN\\IdeaProjects\\nlp\\resources\\dataset\\training";

    public static void main(String[] args) {

            TextDirectoryToArff tdta = new TextDirectoryToArff();
            try {
                Instances dataset = tdta.createDataset(DATA_SET_TRAIN_FILES);
                System.out.println(dataset);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
    }
}
