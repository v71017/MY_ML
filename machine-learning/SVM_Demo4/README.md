# benchmark-ml-java
This repository contains code on benchmarking and sample codes of most popular Machine Learning libraries in Java. It currently contains code for popular SVM algorithms and classification tasks only, but the structure makes it really easy to benchmark other algorithsm.

The idea is to benchmark a new algorithm you should just need to extend a AlgorithmRunner.java and provide implementation of your graph. The rest of work including generating graphs and reports is done by the framework.

## SVM libraries

[LibSVM](https://www.csie.ntu.edu.tw/~cjlin/libsvm/)

[Spark's MLLib](https://spark.apache.org/docs/latest/mllib-linear-methods.html#linear-support-vector-machines-svms)

[SVMlight](http://svmlight.joachims.org/)

