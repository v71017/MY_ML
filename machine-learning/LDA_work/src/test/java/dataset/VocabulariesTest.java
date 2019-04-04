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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import dataset.Vocabularies;

@RunWith(Enclosed.class)
public class VocabulariesTest {
    public static class WhenReadKosDataset {
        Vocabularies sut;

        @Before
        public void setUp() throws Exception {
            sut = new Vocabularies("src/test/resources/vocab.kos.txt");
        }

        @Test
        public void size_returns_6906() throws Exception {
            assertThat(sut.size(), is(6906));
        }
        
        @Test
        public void get_1_returns_vocabulary_aarp() throws Exception {
            assertThat(sut.get(1).toString(), is("aarp"));
        }
        
        @Test
        public void get_3453_returns_knowledge() throws Exception {
            assertThat(sut.get(3453).toString(), is("knowledge"));
        }
        
        @Test
        public void get_6906_returns_vocabulary_zones() throws Exception {
            assertThat(sut.get(6906).toString(), is("zones"));
        }
        
        @Test
        public void get_1_returns_vocabulary_id_1() throws Exception {
            assertThat(sut.get(1).id(), is(1));
        }
        
        @Test
        public void get_3453_returns_id_3453() throws Exception {
            assertThat(sut.get(3453).id(), is(3453));
        }
        
        @Test
        public void get_6906_returns_vocabulary_id_6906() throws Exception {
            assertThat(sut.get(6906).id(), is(6906));
        }
        
        @Test(expected = IndexOutOfBoundsException.class)
        public void get_0_throws_IndexOutOfBoundsException() throws Exception {
            sut.get(0);
        }
        
        @Test(expected = IndexOutOfBoundsException.class)
        public void get_6907_throws_IndexOutOfBoundsException() throws Exception {
            sut.get(6907);
        }
    }
}
