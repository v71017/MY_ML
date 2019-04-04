# Logistic-Regression
Implementation of Logistic Regression classifier model to determine HAM and SPAM emails

Instructions to execute PART-1 : Logistic Regression.
1. make 						       (This will compile the program)
2. java ProgramLR {TRAIN_HAM_DIR} {TRAIN_SPAM_DIR} {TEST_HAM_DIR} {TEST_SPAM_DIR} {stop_words.txt} {LAMBDA}
3. make clean 						(Optional : This will clean compiled .class files)

NOTE: The value of lambda should be very less eg. 0.002 or 0.008
Example:
java ProgramLR train/ham/ train/spam test/ham/ test/spam/ stopWords.txt 0.01
