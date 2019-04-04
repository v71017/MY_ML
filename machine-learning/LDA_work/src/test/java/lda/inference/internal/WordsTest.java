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
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import lda.inference.internal.Words;

import org.junit.Before;
import org.junit.Test;

import dataset.Vocabulary;

public class WordsTest {
    Words sut;
    @Before
    public void setUp() throws Exception {
        List<Vocabulary> list = Arrays.asList(new Vocabulary(1, "a"),
                                              new Vocabulary(2, "b"),
                                              new Vocabulary(3, "c"));
        sut = new Words(list);
    }
    
    @Test
    public void get_0_returns_1_a() throws Exception {
        assertThat(sut.get(0).id(), is(1));
        assertThat(sut.get(0).toString(), is("a"));
    }
    
    @Test
    public void get_2_returns_3_c() throws Exception {
        assertThat(sut.get(2).id(), is(3));
        assertThat(sut.get(2).toString(), is("c"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void get_minus_1_throws_IllegalArgumentException() throws Exception {
        sut.get(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void get_3_throws_IllegalArgumentException() throws Exception {
        sut.get(3);
    }
}
