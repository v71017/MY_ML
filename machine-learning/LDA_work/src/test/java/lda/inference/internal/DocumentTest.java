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

package lda.inference.internal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lda.inference.internal.Document;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import dataset.Vocabulary;

@RunWith(Enclosed.class)
public class DocumentTest {
    public static class Normal {
        Document sut;
        
        @Before
        public void setUp() throws Exception {
            Vocabulary a = new Vocabulary(1, "a");
            Vocabulary b = new Vocabulary(1, "b");
            Vocabulary c = new Vocabulary(1, "c");
            List<Vocabulary> words = Arrays.asList(a, a, a, a, a, a, a, a, a, a, b, b, b, b, b, c, c); 
            sut = new Document(1, 10, words);
            sut.initializeTopicAssignment(0L);
        }
        
        @Test
        public void id_returns_1() throws Exception {
            assertThat(sut.id(), is(1));
        }
        
        @Test
        public void sum_getTopicCount_is_17() throws Exception {
            int sum = 0;
            for (int t = 0; t < 10; ++t) {
                sum += sut.getTopicCount(t);
            }
            assertThat(sum, is(17));
        }
        
        @Test
        public void getDocLength_returns_17() throws Exception {
            assertThat(sut.getDocLength(), is(17));
        }
        
        @Test
        public void getTopicID_returns_number_between_0_9() throws Exception {
            for (int w = 0; w < 17; ++w) {
                assertThat(sut.getTopicID(w), is(both(greaterThanOrEqualTo(0)).and(lessThanOrEqualTo(9))));
            }
        }
        
        @Test
        public void sum_getTheta_is_1() throws Exception {
            double sum = 0.0;
            for (int t = 0; t < 10; ++t) {
                sum += sut.getTheta(t, 0.1, 0.1 * 10);
            }
            assertThat(sum, is(closeTo(1.0, 0.0001)));
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void getTheta_topicID_lt_0_throws_IllegalArgumentException() throws Exception {
            sut.getTheta(-1, 0.1, 0.1 * 10);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void getTheta_hyperparameter_le_0_throws_IllegalArgumentException() throws Exception {
            sut.getTheta(0, 0.0, 0.0 * 10);
        }
    }
    
    public static class Constructors {
        @Test(expected = IllegalArgumentException.class)
        public void when_id_le_0_throws_IllegalArgumentException() throws Exception {
            new Document(0, 10, new ArrayList<>());
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void when_numTopics_le_0_throws_IllegalArgumentException() throws Exception {
            new Document(1, 0, new ArrayList<>());
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void when_id_and_numTopics_le_0_throws_IllegalArgumentException() throws Exception {
            new Document(0, 0, new ArrayList<>());
        }
    }
}
