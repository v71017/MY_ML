# tweet-analyzer

### Usage

Requires java 1.8

```cli
mvn clean install
mvn exec:java
```

### Configuration

```
# all relative to src/main/resources/
stop.word.path=stopwords.txt
twitter.data.path=twitter_test_data_big_.csv.zip
imdb.data.path.positive=rt-polarity.pos
imdb.data.path.negative=rt-polarity.neg

runner.type=STATISTICAL|INTERACTIVE
loader.type=TWITTER|IMDB
classification.type=CUSTOM_MULTINOMIAL_BAYES|CUSTOM_BERNULLI_BAYES|WEKA_BAYES|WEKA_J48

#iteration count for statistics
runner.cross-validation.iteration.count=10

hold.out.training.data.proportion=70

# specify seed to test multiple classifiers on same data
data.randomize.seed=1
# limit data so that weka does not take forever to compute
data.size.limit=10000
```


### Links and used literature:

**Naive Bayes**:
- Article: http://sebastianraschka.com/Articles/2014_naive_bayes_1.html
- Book: http://nlp.stanford.edu/IR-book/pdf/13bayes.pdf

**Weka**:
- http://jmgomezhidalgo.blogspot.com.es/2013/06/baseline-sentiment-analysis-with-weka.html
- http://stackoverflow.com/questions/9707825/basic-text-classification-with-weka-in-java
- https://weka.wikispaces.com/Use+WEKA+in+your+Java+code

**Data**:
- tweets: http://help.sentiment140.com/for-students
- movies: https://www.cs.cornell.edu/people/pabo/movie-review-data/

### Some Experimentation results

Multinomial bayes (Twitter CSV 1000) (time < 1 min):
```
[sentiment=POSITIVE,precision=58,recall=60]
[sentiment=NEGATIVE,precision=66,recall=64]
```
Bernulli bayes (Twitter CSV 1000) (time < 1 min):
```
[sentiment=POSITIVE,precision=57,recall=71]
[sentiment=NEGATIVE,precision=70,recall=55]
```
Weka J48 (Twitter CSV 1000) (time ~ 3 min):
```
[sentiment=POSITIVE,precision=58,recall=68]
[sentiment=NEGATIVE,precision=68,recall=58]
```
Weka Naive Bayes (Twitter CSV 1000) (time ~ 1 min):
```
[sentiment=POSITIVE,precision=59,recall=57]
[sentiment=NEGATIVE,precision=65,recall=67]
```
Weka Naive Bayes without NGrams (Twitter CSV 1000) (time ~ 1 min):
```
[sentiment=POSITIVE,precision=62,recall=63]
[sentiment=NEGATIVE,precision=68,recall=68]
```
