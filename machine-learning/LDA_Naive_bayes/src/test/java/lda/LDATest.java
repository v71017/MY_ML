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

package lda;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import lda.inference.InferenceMethod;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import dataset.Dataset;

@RunWith(Enclosed.class)
public class LDATest {
    public static class WhenReadKosDataset {
        LDA sut;
        
        @Before
        public void setUp() throws Exception {
            Dataset dataset = new Dataset("src/test/resources/docword.kos.txt", "src/test/resources/vocab.kos.txt"); 
            sut = new LDA(0.1, 0.1, 10, dataset, InferenceMethod.CGS);
        }
        
        @After
        public void tearDown() throws Exception {
           sut = null;
        }
        
        @Test
        public void getBow_returns_bow_numDocs_3430_numVocabs_6906_numWords_353160() throws Exception {
            assertThat(sut.getBow().getNumDocs(),   is(3430));
            assertThat(sut.getBow().getNumVocabs(), is(6906));
            assertThat(sut.getBow().getNumNNZ(),  is(353160));
        }
        
        @Test
        public void getVocab_1_returns_aarp() throws Exception {
            assertThat(sut.getVocab(1), is("aarp"));
        }
    }
}
