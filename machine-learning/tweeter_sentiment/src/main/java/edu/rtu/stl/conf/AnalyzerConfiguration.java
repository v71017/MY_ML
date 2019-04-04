package edu.rtu.stl.conf;

import edu.rtu.stl.analyzer.BaseLineAnalyzer;
import edu.rtu.stl.analyzer.BayesAnalyzer;
import edu.rtu.stl.analyzer.KnnTfidfAnalyzer;
import edu.rtu.stl.analyzer.WekaAnalyzer;
import edu.rtu.stl.classifier.*;
import edu.rtu.stl.domain.*;
import edu.rtu.stl.parser.*;
import edu.rtu.stl.runner.Loader;
import edu.rtu.stl.util.WithDefaultProperties;
import edu.rtu.stl.util.WithFileReading;
import org.apache.commons.collections4.ListUtils;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static edu.rtu.stl.domain.LoaderType.IMDB;
import static edu.rtu.stl.domain.LoaderType.TWITTER;
import static edu.rtu.stl.domain.Sentiment.negative;
import static edu.rtu.stl.domain.Sentiment.positive;
import static java.util.Arrays.asList;

public class AnalyzerConfiguration implements WithFileReading, WithDefaultProperties {

    private final Set<String> stopWords = readStopWords();
    public final Tokenizer tokenizer = new Tokenizer(stopWords);
    public final UniqueTokenizer uniqueTokenizer = new UniqueTokenizer(stopWords);
    public final List<ClassificationConfiguration> classificationConfigurations = asList(multinomialBayes(), bernulliBayes(), wekaBayes(), wekaJ48(), tfidf(), baseline());
    public final List<Loader> loaders = asList(twitterLoader(), imdbLoader());

    public ClassificationConfiguration<BayesDataSet> multinomialBayes() {
        return new ClassificationConfiguration<>(ClassificationType.CUSTOM_MULTINOMIAL_BAYES, new BayesAnalyzer(tokenizer),
                new MultinomialNaiveBayesClassifier(tokenizer));
    }

    public ClassificationConfiguration<BayesDataSet> bernulliBayes() {
        return new ClassificationConfiguration<>(ClassificationType.CUSTOM_BERNULLI_BAYES, new BayesAnalyzer(uniqueTokenizer),
                new BernulliNaiveBayesClassifier(uniqueTokenizer));
    }

    public ClassificationConfiguration<WekaDataSet> wekaBayes() {
        return new ClassificationConfiguration<>(ClassificationType.WEKA_BAYES, new WekaAnalyzer(),
                setUpWekaClassifier(new NaiveBayes()));
    }

    public ClassificationConfiguration<WekaDataSet> wekaJ48() {
        return new ClassificationConfiguration<>(ClassificationType.WEKA_J48, new WekaAnalyzer(),
                setUpWekaClassifier(new J48()));
    }

    public ClassificationConfiguration<KnnTfidfDataSet> tfidf() {
        return new ClassificationConfiguration<>(ClassificationType.CUSTOM_KNN_TFIDF, new KnnTfidfAnalyzer(tokenizer),
                new KnnTfidfClassifier(tokenizer));
    }

    public ClassificationConfiguration<BaselineDataSet> baseline() {
        return new ClassificationConfiguration<>(ClassificationType.BASELINE, new BaseLineAnalyzer(),
                new BaselineClassifier());
    }

    public WekaClassifier setUpWekaClassifier(Classifier wekaClassifier) {

        Ranker searchClass = new Ranker();
        searchClass.setThreshold(0.0);

        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setDelimiters("\\s+");
        tokenizer.setNGramMaxSize(1);
        tokenizer.setNGramMinSize(1);

        StringToWordVector filter1 = new StringToWordVector();
        filter1.setTokenizer(tokenizer);
        filter1.setWordsToKeep(10 * 1000 * 1000);
        filter1.setDoNotOperateOnPerClassBasis(true);

        AttributeSelection filter2 = new AttributeSelection();
        filter2.setEvaluator(new InfoGainAttributeEval());
        filter2.setSearch(searchClass);

        MultiFilter multiFilter = new MultiFilter();
        multiFilter.setFilters(new Filter[]{filter1, filter2});

        FilteredClassifier classifier = new FilteredClassifier();
        classifier.setFilter(multiFilter);
        classifier.setClassifier(wekaClassifier);

        return new WekaClassifier(classifier);
    }

    public Loader twitterLoader() {
        return new Loader(TWITTER) {
            @Override
            public List<Document> getFullData() {
                return new TwitterCSVParser().parse(FILE_READER.readLines(BASE_PATH.resolve(
                        getProperty("twitter.data.path"))));
            }
        };
    }

    public Loader imdbLoader() {
        return new Loader(IMDB) {
            @Override
            public List<Document> getFullData() {
                Parser positiveParser = new ImdbReviewParser(positive);
                Parser negativeParser = new ImdbReviewParser(negative);

                Path positiveData = BASE_PATH.resolve(getProperty("imdb.data.path.positive"));
                Path negativeData = BASE_PATH.resolve(getProperty("imdb.data.path.negative"));

                return ListUtils.union(
                        positiveParser.parse(FILE_READER.readLines(positiveData)),
                        negativeParser.parse(FILE_READER.readLines(negativeData))
                );
            }
        };
    }

}
