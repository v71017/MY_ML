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
import static org.hamcrest.MatcherAssert.*;
import lda.inference.internal.TopicAssignment;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class TopicAssignmentTest {
    public static class WhenInitialize {
        final int numWords = 100;
        TopicAssignment sut = new TopicAssignment();

        @Before
        public void setUp() throws Exception {
            sut.initialize(numWords, 10, 0);
        }

        @Test
        public void each_element_is_greater_than_or_equal_to_0() throws Exception {
            for (int i = 0; i < numWords; ++i) {
                assertThat(sut.get(i), is(greaterThanOrEqualTo(0)));
            }
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void get_minus_1_throws_IllegalArgumentException() throws Exception {
            sut.get(-1);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void get_100_throws_IllegalArgumentException() throws Exception {
            sut.get(100);
        }
        
        @Test
        public void set_0_1_and_get_0_returns_1() throws Exception {
            sut.set(0, 1);
            assertThat(sut.get(0), is(1));
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void set_minum_1_1_throws_IllegalArgumentException() throws Exception {
            sut.set(-1, 1);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void set_100_1_throws_IllegalArgumentException() throws Exception {
            sut.set(100, 1);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void set_0_minus_1_throws_IllegalArgumentException() throws Exception {
            sut.set(0, -1);
        }
    }
    
    public static class Other {
        TopicAssignment sut = new TopicAssignment();
        
        @Test(expected = IllegalArgumentException.class)
        public void initialize_0_10_0_throws_IllegalArgumentException() throws Exception {
            sut.initialize(0, 10, 0);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void initialize_10_0_0_throws_IllegalArgumentException() throws Exception {
            sut.initialize(10, 0, 0);
        }
        
        @Test(expected = IllegalStateException.class)
        public void get_0_when_not_initialized_throws_IllegalStateException() throws Exception {
            sut.get(0);
        }
        
        @Test(expected = IllegalStateException.class)
        public void set_0_0_when_not_initialized_throws_IllegalStateException() throws Exception {
            sut.set(0, 0);
        }
    }
}
