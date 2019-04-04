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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import lda.inference.InferenceProperties;

import org.junit.Before;
import org.junit.Test;

public class InferencePropertiesTest {
    InferenceProperties sut;
    @Before
    public void setUp() throws Exception {
        sut = new InferenceProperties();
        sut.load("src/test/resources/lda.properties");
    }

    @Test
    public void numIteration_is_50() throws Exception {
        assertThat(sut.numIteration(), is(50));
    }

    @Test
    public void seed_is_100() throws Exception {
        assertThat(sut.seed(), is(100L));
    }
}
