package preprocessing;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dewadkar on 8/3/2016.
 */
public class TfIdfVector {
    public double termFrequency() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new FileReader("resources/weather.txt"));
            Instances data = new Instances(reader);
            reader.close();
            // setting class attribute
            data.setClassIndex(data.numAttributes() - 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
