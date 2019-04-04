/*
* Copyright 2015 Kohei Yamamoto
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package lda.inference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InferenceProperties {
    public static String PROPERTIES_FILE_NAME = "/Users/vpati/Intuit_project/ml/lda/LDA/src/test/resources/lda.properties";
    
    PropertiesLoader loader = new PropertiesLoader();
    private Properties properties;
    
    public InferenceProperties() {
        this.properties = new Properties();
    }
    
    /**
     * Load properties.
     * @param fileName
     * @throws IOException
     * @throws NullPointerException fileName is null
     */
    public void load(String fileName) throws IOException {
        if (fileName == null) throw new NullPointerException();
        InputStream stream = loader.getInputStream(fileName);
        if (stream == null) throw new NullPointerException();
        properties.load(stream);
    }
    
    public Long seed() {
        return Long.parseLong(properties.getProperty("seed"));
    }

    public Integer numIteration() {
        return Integer.parseInt(properties.getProperty("numIteration"));
    }
}

class PropertiesLoader {
    public InputStream getInputStream(String fileName) throws FileNotFoundException {
        if (fileName == null) throw new NullPointerException();
        return new FileInputStream(fileName);
    }
}
