/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubes.ml.pkg1;

/*Import*/
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Alriana
 */
public class fileReader {
   
    String filepath;
    public fileReader(String _filepath){
        filepath=_filepath;
    }
    Instances data;
    
    public void setFilepath(String _filepath){
        filepath = _filepath;
    }
   public void read() throws FileNotFoundException, IOException{
       
       BufferedReader reader = new BufferedReader(new FileReader(filepath));
       
       data = new Instances(reader);
       reader.close();
       
       data.setClassIndex(data.numAttributes()-1);
   }
   
   public Instances getData(){
       return data;
   }
}
