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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import lda.LDA;
import lda.inference.InferenceMethod;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import dataset.Dataset;

@RunWith(Enclosed.class)
public class CollapsedGibbsSamplerTest {
    public static class WhenReadKosDataset {
        CollapsedGibbsSampler sut;
        LDA lda;

        @Before
        public void setUp() throws Exception {
            Dataset dataset = new Dataset("src/test/resources/docword.kos.txt", "src/test/resources/vocab.kos.txt"); 

            lda = new LDA(0.1, 0.1, 10, dataset, InferenceMethod.CGS);
            sut = new CollapsedGibbsSampler();
            sut.setNumIteration(100);
        }
        
        @After
        public void tearDown() throws Exception {
           sut = null;
        }
        
        @Test
        public void before_setUp_ready_is_false() throws Exception {
            assertThat(sut.isReady(), is(false));
        }

        @Test
        public void after_setUp_ready_is_true() throws Exception {
            sut.setUp(lda);
            assertThat(sut.isReady(), is(true));
        }

        @Test
        public void getNumIteration_return_100() throws Exception {
            assertThat(sut.getNumIteration(), is(100));            
        }

        @Test
        public void after_setUp_getDTCount_returns_numTopics_items_non_negative_number() throws Exception {
            sut.setUp(lda);
            
            for (int d = 1; d <= lda.getBow().getNumDocs(); ++d) {
                for (int t = 0; t < lda.getNumTopics(); ++t) {
                    assertThat(sut.getDTCount(d, t), greaterThanOrEqualTo(0));
                }
            }
        }

        @Test
        public void after_setUp_getTVCount_return_non_negative_number() throws Exception {
            sut.setUp(lda);

            for (int t = 0; t < lda.getNumTopics(); ++t) {
                for (int v = 1; v <= lda.getBow().getNumVocabs(); ++v) {
                    assertThat(sut.getTVCount(t, v), is(greaterThanOrEqualTo(0)));
                }
            }
        }

        @Test
        public void after_setUp_getTSum_equals_getNumWords() throws Exception {
            sut.setUp(lda);
            
            int sum = 0;
            for (int t = 0; t < lda.getNumTopics(); ++t) {
                sum += sut.getTSumCount(t);
            }
            assertThat(sum, is(lda.getBow().getNumWords()));
        }

        @Test
        public void when_runSampling_sum_getTheta_for_all_topics_is_1() throws Exception {
            sut.setUp(lda);
            sut.runSampling();
            
            for (int d = 1; d <= lda.getBow().getNumDocs(); ++d) {
                double sum = 0.0;
                for (int t = 0; t < lda.getNumTopics(); ++t) {
                    sum += sut.getTheta(d, t);
                }
                assertThat(sum, is(closeTo(1.0, 0.0001)));
            }
        }
        
        @Test
        public void when_runSampling_sum_getPhi_for_all_vocabs_is_1() throws Exception {
            sut.setUp(lda);
            sut.runSampling();

            for (int t = 0; t < lda.getNumTopics(); ++t) {
                double sum = 0.0;
                for (int v = 1; v <= lda.getBow().getNumVocabs(); ++v) {
                    sum += sut.getPhi(t, v);
                }
                assertThat(sum, is(closeTo(1.0, 0.0001)));
            }
        }
        
        @Test
        public void when_runSampling_sum_fullConditional_is_1() throws Exception {
            sut.setUp(lda);
            sut.runSampling();

            for (int d = 1; d < lda.getBow().getNumDocs(); ++d) {
                double sum = 0.0;
                for (int t = 0; t < lda.getNumTopics(); ++t) {
                    for (int v = 1; v <= lda.getBow().getNumVocabs(); ++v) {
                        sum += sut.getTheta(2, t) * sut.getPhi(t, v);
                    }
                }
                assertThat(sum, is(closeTo(1.0, 0.0001)));
            }
        }
        
        @Test
        public void when_getFullConditionalDistribution_sum_is_1() throws Exception {
            sut.setUp(lda);
            sut.runSampling();
            
            IntegerDistribution distribution
                = sut.getFullConditionalDistribution(lda.getNumTopics(), 1, 1);
            assertThat(distribution.cumulativeProbability(lda.getNumTopics() - 1), is(closeTo(1.0, 0.001)));
        }
    }
}
