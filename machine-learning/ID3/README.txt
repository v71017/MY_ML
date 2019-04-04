
GELLI FRANCESCO - ID3 IMPLEMENTATION


The project has been deployed in Java, using the platform Eclipse. It provide an implementation from scratch of ID3 Machine Learning algorithm, using the Open Source Project Weka for data representation.
Given a dataset (.arff), a decision tree will be built on a Training Set, and a Testing Set will be used for evaluating the performances.
Test results are reported in the attached document "Report.pdf" (only in Italian lang).
In this work Missing Values in datasets are managed.


For this project, Maven hasn't been used, so for compiling the Weka library included (.jar) is required.

Samples of datasets (ARFF format) are included in "Data" folder.

This project hasn't a GUI, for changing dataset change the appropriate string in "ID3Project" class, where indicated.

The Training/Testing Set split is made with a policy of 70-30%. Tune it in "ManagerDataSet" class. 


