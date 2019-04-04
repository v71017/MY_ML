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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import lda.inference.internal.VocabularyCounter;

import org.junit.Before;
import org.junit.Test;

public class VocabularyCounterTest {
    final int size = 100;
    VocabularyCounter sut = new VocabularyCounter(size);
    
    @Before
    public void setUp() throws Exception {
        for (int i = 1; i <= size; ++i) {
            for (int j = 0; j < 10; ++j) sut.incrementVocabCount(i);
        }
    }
    
    @Test
    public void getSumCount_returns_1000() throws Exception {
        assertThat(sut.getSumCount(), is(1000));
    }
    
    @Test
    public void getVocabCount_1_returns_10() throws Exception {
        assertThat(sut.getVocabCount(1), is(10));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getVocabCount_0_throws_IllegalArgumentException() throws Exception {
        sut.getVocabCount(0);
    }
    
    @Test
    public void getVocabCount_100_returns_10() throws Exception {
        assertThat(sut.getVocabCount(100), is(10));
    }
    
    @Test
    public void getVocabCount_101_returns_0() throws Exception {
        assertThat(sut.getVocabCount(101), is(0));
    }
}
