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

package alltests;

import lda.LDATest;
import lda.inference.InferencePropertiesTest;
import lda.inference.internal.AssignmentCounterTest;
import lda.inference.internal.CollapsedGibbsSamplerTest;
import lda.inference.internal.DocumentTest;
import lda.inference.internal.TopicAssignmentTest;
import lda.inference.internal.TopicCounterTest;
import lda.inference.internal.TopicTest;
import lda.inference.internal.VocabularyCounterTest;
import lda.inference.internal.WordsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dataset.BagOfWordsTest;
import dataset.VocabulariesTest;

@RunWith(Suite.class)
@SuiteClasses({AssignmentCounterTest.class,
               BagOfWordsTest.class,
               DocumentTest.class,
               LDATest.class,
               CollapsedGibbsSamplerTest.class,
               InferencePropertiesTest.class,
               TopicAssignmentTest.class,
               TopicCounterTest.class,
               TopicTest.class,
               VocabulariesTest.class,
               VocabularyCounterTest.class,
               WordsTest.class})
public class AllTests {
}
