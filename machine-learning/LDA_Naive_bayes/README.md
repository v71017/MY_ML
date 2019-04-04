LDA in Java 8
=============

Latent Dirichlet Allocation in Java 8.

Latent Dirichlet Allocation (LDA) [Blei+ 2003] is the basic probabilistic topic model.
Please see following for more details:

- [Latent Dirichlet allocation - Wikipedia, the free encyclopedia](http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation)

Now, this software supports [collapsed Gibbs sampling](http://psiexp.ss.uci.edu/research/papers/sciencetopics.pdf) [Griffiths and Steyvers 2004] for model inference.

This repository includes dataset from [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/datasets) [Lichman 2013].

Requierments
------------

- Java 8
- Apache Commons
  - Math
  - Lang
- Maven

For unit testing, these libraries are also needed.

- JUnit
- Mockito

Usage
-----

### Dataset Form

The form of bag-of-words dataset follows [Bag of Words Data Set](https://archive.ics.uci.edu/ml/datasets/Bag+of+Words) in [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/index.html).
The form of doc-vocab-count dataset is following:

    #Documents
    #Vocabularies
    #NonZeros
    docID vocabID count
    docID vocabID count
    ...
    docID vocabID count

The form of vocabularies dataset is following:

    vocab1
    vocab2
    vocab3
    ...
    vocabN

Each number of lines is `vocabID`.

### Example

There is `lda.BagOfWords` to read dataset from files.
`lda.BagOfWords` object and other parameters are passed to initialize `lda.LDA`.
For example:

    Dataset dataset = new Dataset("path/to/doc-vocab-counts", "path/to/vocabs");
    LDA lda = new LDA(0.1                    /* initial alpha */,
                      0.1                    /* initial beta */,
                      50                     /* the number of topics */,
                      bow                    /* bag-of-words */,
                      CGS                    /* use collapsed Gibbs sampler for inference */,
                      "path/to/properties"   /* properties file path */);
    lda.run();

These items are available as properties:

    numIteration=<the number of iteration of collapsed Gibbs sampling>
    seed=<seed for the pseudo random number generator>

The results of topics can be refered as follows:

    List<Pair<String, Double>> vocabs
        = LDA.getVocabsSortedByPhi(0 /* = topic ID */);
    vocabs.get(0).getLeft();  // the largest probability vocabulary in topic-0
    vocabs.get(0).getRight(); // the probability value of the above vocabulary

Please see `example.Example#main` for more details.
Execute these commands at the directory `LDA` to build and run `example.Example#main`.

    $ mvn clean package dependency:copy-dependencies -DincludeScope=runtime
    $ java -jar target/LDA-<version>.jar

License
-------

- [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
