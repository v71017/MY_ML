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
import lda.inference.internal.AssignmentCounter;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class AssignmentCounterTest {
    public static class WhenSize100IncEachElem10 {
        AssignmentCounter sut;
        final int size = 100;
        
        @Before
        public void setUp() throws Exception {
            sut = new AssignmentCounter(size);
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < 10; ++j) sut.increment(i);
            }
        }
        
        @Test
        public void size_returns_100() throws Exception {
            assertThat(sut.size(), is(100));
        }
        
        @Test
        public void getCount_returns_10() throws Exception {
            for (int i = 0; i < size; ++i) {
                assertThat(sut.get(i), is(10));
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
        public void getSum_returns_1000() throws Exception {
            assertThat(sut.getSum(), is(1000));
        }
    }
    
    public static class WhenSize100IncEachElem10DecEvenElem5 {
        AssignmentCounter sut;
        final int size = 100;
        
        @Before
        public void setUp() throws Exception {
            sut = new AssignmentCounter(size);
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < 10; ++j) sut.increment(i);
            }
            for (int i = 0; i < size; i += 2) {
                for (int j = 0; j < 5; ++j) sut.decrement(i);
            }            
        }
        
        @Test
        public void size_returns_100() throws Exception {
            assertThat(sut.size(), is(100));
        }
        
        @Test
        public void get_returns_5_for_even_id() throws Exception {
            for (int i = 0; i < size; i += 2) {
                assertThat(sut.get(i), is(5));
            }
        }
        
        @Test
        public void get_returns_10_for_odd_id() throws Exception {
            for (int i = 1; i < size; i += 2) {
                assertThat(sut.get(i), is(10));
            }
        }
        
        @Test
        public void getSum_returns_750() throws Exception {
            assertThat(sut.getSum(), is(750));
        }
    }
    
    public static class Other {
        @Test(expected = IllegalArgumentException.class)
        public void AssignmentCounter_0_throws_IllegalArgumentException() throws Exception {
            new AssignmentCounter(0);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void AssignmentCounter_minus_1_throws_IllegalArgumentException() throws Exception {
            new AssignmentCounter(-1);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void increment_minus_1_throws_IllegalArgumentException() throws Exception {
            AssignmentCounter sut = new AssignmentCounter(100);
            sut.increment(-1);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void increment_100_throws_IllegalArgumentException() throws Exception {
            AssignmentCounter sut = new AssignmentCounter(100);
            sut.increment(100);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void decrement_minus_1_throws_IllegalArgumentException() throws Exception {
            AssignmentCounter sut = new AssignmentCounter(100);
            sut.decrement(-1);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void decrement_100_throws_IllegalArgumentException() throws Exception {
            AssignmentCounter sut = new AssignmentCounter(100);
            sut.decrement(100);
        }

        @Test(expected = IllegalStateException.class)
        public void decrement_in_initial_state_throws_IllegalStateException() throws Exception {
            AssignmentCounter sut = new AssignmentCounter(100);
            sut.decrement(50);
        }
        
        @Test(expected = IllegalStateException.class)
        public void decrement_in_count_0_throws_IllegalStateException() throws Exception {
            AssignmentCounter sut = new AssignmentCounter(100);
            for (int i = 0; i < 10; ++i) sut.increment(50);
            for (int i = 0; i < 11; ++i) sut.decrement(50);
        }
    }
}
