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

package dataset;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is immutable.
 */
public final class BagOfWords {
    private DatasetLoader loader;

    private final int numDocs;
    private final int numVocabs;
    private final int numNNZ;
    private final int numWords;

    // docID -> the vocabs sequence in the doc
    private Map<Integer, List<Integer>> words;

    // docID -> the doc length 
    private Map<Integer, Integer> docLength;

    /**
     * Read the bag-of-words dataset.
     * @param filePath
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    public BagOfWords(String filePath) throws FileNotFoundException, IOException, Exception {
        this(filePath, null);
    }
    
    /**
     * Read the bag-of-words dataset.
     * @param filePath
     * @param loader
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     * @throws NullPointerException filePath is null
     */
    public BagOfWords(String filePath, DatasetLoader loader) throws FileNotFoundException, IOException, Exception {
        if (filePath == null) throw new NullPointerException();
        
        if (loader == null) this.loader = new DatasetLoader();
        else this.loader = loader;

        this.words     = new HashMap<>();
        this.docLength = new HashMap<>();

        BufferedReader reader
            = new BufferedReader(new InputStreamReader(this.loader.getInputStream(filePath)));

        int numDocs     = 0;
        int numVocabs   = 0;
        int numNNZ      = 0;
        int numWords    = 0;
        int headerCount = 2;
        String s = null;
        while ((s = reader.readLine()) != null) {
            List<Integer> numbers
            = Arrays.asList(s.split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());

            if (numbers.size() == 1) {
                if (headerCount == 2)      numDocs   = numbers.get(0);
                else if (headerCount == 1) numVocabs = numbers.get(0);
                else if (headerCount == 0) numNNZ    = numbers.get(0);
                --headerCount;
                continue;
            }
            else if (numbers.size() == 3) {
                final int docID   = numbers.get(0);
                final int vocabID = numbers.get(1);
                final int count   = numbers.get(2);

                // Set up the words container
                if (!words.containsKey(docID)) {
                    words.put(docID, new ArrayList<>());
                }  
                for (int c = 0; c < count; ++c) {
                    words.get(docID).add(vocabID);
                }

                // Set up the doc length map
                Optional<Integer> currentCount
                    = Optional.ofNullable(docLength.putIfAbsent(docID, count));
                currentCount.ifPresent(c -> docLength.replace(docID, c + count));

                numWords += count;
            }
            else {
                throw new Exception("Invalid dataset form was detected.");
            }
        }
        reader.close();

        this.numDocs   = numDocs;
        this.numVocabs = numVocabs;
        this.numNNZ    = numNNZ;
        this.numWords  = numWords;
    }

    public int getNumDocs() {
        return numDocs;
    }

    /**
     * Get the length of the document.
     * @param docID
     * @return length of the document
     * @throws IllegalArgumentException docID <= 0 || #documents < docID
     */
    public int getDocLength(int docID) {
        if (docID <= 0 || getNumDocs() < docID) {
            throw new IllegalArgumentException();
        }

        return docLength.get(docID);
    }

    /**
     * Get the unmodifiable list of words in the document.
     * @param docID
     * @return the unmodifiable list of words
     * @throws IllegalArgumentException docID <= 0 || #documents < docID
     */
    public List<Integer> getWords(final int docID) {
        if (docID <= 0 || getNumDocs() < docID) {
            throw new IllegalArgumentException();
        }
        return Collections.unmodifiableList(words.get(docID));
    }

    public int getNumVocabs() {
        return numVocabs;
    }

    public int getNumNNZ() {
        return numNNZ;
    }

    public int getNumWords() {
        return numWords;
    }
}

class DatasetLoader {
    public InputStream getInputStream(String fileName) throws FileNotFoundException {
        if (fileName == null) throw new NullPointerException();
        return new FileInputStream(fileName);
    }
}
