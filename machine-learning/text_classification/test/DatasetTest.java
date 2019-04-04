package preprocessing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dewadkar on 8/5/2016.
 */
public class DatasetTest {

    private String RAW_DATA_FILE_PATH = "resources/rawDataSet/auto.dat.train";

    @Test
    public void testTextToCSVConvertFunction(){
        DataSet dataSet= new DataSet();

//        assertEquals("",dataSet.createFilesDataSetFromSingleFile(RAW_DATA_FILE_PATH));
    }

}
