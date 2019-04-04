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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import dataset.BagOfWords;

@RunWith(Enclosed.class)
public class BagOfWordsTest {
    public static class WhenReadKosDataset {
        BagOfWords sut;

        @Before
        public void setUp() throws Exception {
            sut = new BagOfWords("src/test/resources/docword.kos.txt");
        }

        @Test
        public void getNumVocabs_returns_6906() throws Exception {
            assertThat(sut.getNumVocabs(), is(6906));
        }

        @Test
        public void getNumDocuments_returns_3430() throws Exception {
            assertThat(sut.getNumDocs(), is(3430));
        }

        @Test
        public void getNumNNZ_returns_353160() throws Exception {
            assertThat(sut.getNumNNZ(), is(353160));
        }

        @Test
        public void getDocLength_1_returns_137() throws Exception {
            assertThat(sut.getDocLength(1), is(137));
        }
        
        @Test
        public void getDocLength_3430_returns_85() throws Exception {
            assertThat(sut.getDocLength(3430), is(85));
        }

        @Test(expected = IllegalArgumentException.class)
        public void getDocLength_0_throws_IllegalArgumentException() throws Exception {
            sut.getDocLength(0);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void getDocLength_3431_throws_IllegalArgumentException() throws Exception {
            sut.getDocLength(3431);
        }
        
        @Test
        public void getWords_1_returns_list_size_137() throws Exception {
            List<Integer> list = sut.getWords(1);
            assertThat(list.size(), is(137));
        }
        
        @Test
        public void getWords_1_returns_list_having_only_valid_vocabID() throws Exception {
            List<Integer> list = sut.getWords(1);
            for (int v : list) {
                assertThat(v, is(greaterThanOrEqualTo(1)));
                assertThat(v, is(lessThanOrEqualTo(6906)));
            }
        }
        
        @Test
        public void getWords_3430_returns_list_size_85() throws Exception {
            List<Integer> list = sut.getWords(3430);
            assertThat(list.size(), is(85));
        }
        
        @Test
        public void getWords_3430_returns_list_having_only_valid_vocabID() throws Exception {
            List<Integer> list = sut.getWords(3430);
            for (int v : list) {
                assertThat(v, is(greaterThanOrEqualTo(1)));
                assertThat(v, is(lessThanOrEqualTo(6906)));
            }
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void getWords_0_throws_IllegalArgumentException() throws Exception {
            sut.getWords(0);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void getWords_3431_throws_IllegalArgumentException() throws Exception {
            sut.getWords(3431);
        }
    }
}
