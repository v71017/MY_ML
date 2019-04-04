
import java.io.*;
import java.util.StringTokenizer;

public class SimpleSVM2 {

	public static double[] calc_CostAndGrad(int numDim, double[][] train_features, int[] train_labels, double[] COST,
			int f, double lambda, double[] w) {

		long startTime = System.nanoTime();

		int size = train_features.length;
		double[] ypp = new double[size];
		double cost = 0;
		for (int m = 0; m < size; m++) {
			ypp[m] = 0;
			// Inner product: w * x
			for (int d = 0; d < numDim; d++)
				ypp[m] += train_features[m][d] * w[d];

			// Empirical loss
			if (train_labels[m] * ypp[m] - 1 < 0) {
				cost += (1 - train_labels[m] * ypp[m]);
			}
		}

		double[] grad_p = new double[numDim];
		// ∇(j) = w.r.t. w(j)
		for (int d = 0; d < numDim; d++) {
			grad_p[d] = 0;
			for (int m = 0; m < size; m++)
				if (train_labels[m] * ypp[m] - 1 < 0)
					grad_p[d] -= train_labels[m] * train_features[m][d];
		}

		if (f == 0)
			for (int d = 0; d < numDim; d++) {
				grad_p[d] += Math.abs(lambda * w[d]);
				cost += 0.5 * lambda * w[d] * w[d];
			}

		COST[0] = cost;

		long estimatedTime = System.nanoTime() - startTime;
		double seconds = (double) estimatedTime / 1000000000.0;
		System.out.printf("[INFO] - partial_grad -> Time elapsed: %.2f seconds\n\n", seconds);

		return grad_p;
	}

	public static void accumulateCostAndGrad(double[] grad, double[] grad2, double[] COST, double[] COST2) {
		for (int d = 0; d < grad2.length; d++)
			grad[d] += grad2[d];

		COST[0] += COST2[0];
	}

	public static void updateWeight(double lr, double[] grad_p, double[] w) {

		// w(j) ← w(j) - η ∇J(j)(xi)
		for (int d = 0; d < w.length; d++) {
			w[d] -= lr * grad_p[d];
		}

	}

	public static int predict(double[] x, double[] w) {
		double pre = 0;
		for (int j = 0; j < x.length; j++) {
			pre += x[j] * w[j];
		}
		if (pre >= 0)
			return 1;
		else
			return -1;
	}

	public static int[] predict_chunck(double[][] testX, double[] w) {
		long startTime = System.nanoTime();

		int[] label_result = new int[testX.length];
		for (int i = 0; i < testX.length; i++) {
			label_result[i] = predict(testX[i], w);

		}

		long estimatedTime = System.nanoTime() - startTime;
		double seconds = (double) estimatedTime / 1000000000.0;
		System.out.printf("[INFO] - predict_chunck -> Time elapsed: %.2f seconds\n\n", seconds);

		return label_result;
	}

	// savePredictionToFile: to save the predicition in a file
	public static void savePredictionToFile(int[] result, String filename) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			for (int i = 0; i < result.length; i++) {
				if (result[i] != 0) {
					String value = result[i] + "\n";
					fos.write(value.getBytes());
				}
			}
		} catch (IOException ioe) {
			System.out.println("[ERROR] - savePredictionToFile");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					System.out.println("[ERROR] - savePredictionToFile");
				}
			}
		}
	}

	public static void loadfile_and_split_test(double[][][] features, String fileName, int sizeTrainPerFrag,
			int numTotal) {

		long startTime = System.nanoTime();
		try {
			BufferedReader lines = new BufferedReader(new FileReader(fileName));
			StringTokenizer tokenizer;

			int index = 0;
			int f = -1;
			for (int i = 0; i < numTotal; i++) {
				String line = lines.readLine();

				if ((index % sizeTrainPerFrag) == 0) {
					f++;
					index = 0;
				}

				tokenizer = new StringTokenizer(line, ",");
				if (tokenizer.countTokens() > 1) {
					int label = (int) Float.parseFloat(tokenizer.nextToken());
					int index2 = 0;
					while (tokenizer.hasMoreTokens()) {
						features[f][index][index2] = Double.parseDouble(tokenizer.nextToken());
						index2++;
					}
				}

				index++;
			}
			lines.close();
			long estimatedTime = System.nanoTime() - startTime;
			double seconds = (double) estimatedTime / 1000000000.0;
			System.out.printf("[INFO] loadfile_and_split_test ->Time elapsed: %.2f seconds\n\n", seconds);

		} catch (FileNotFoundException e) {
			System.out.println("ERROR - SVM.loadfile_and_split_test");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR - SVM.loadfile_and_split_test");
			e.printStackTrace();
		}
	}

	public static void loadfile_and_split_train(double[][][] features, int[][] labels, String fileName,
			int sizeTrainPerFrag, int numTotal) {

		long startTime = System.nanoTime();

		try {
			BufferedReader lines = new BufferedReader(new FileReader(fileName));
			StringTokenizer tokenizer;

			int index = 0;
			int f = -1;
			for (int i = 0; i < numTotal; i++) {
				String line = lines.readLine();

				if ((index % sizeTrainPerFrag) == 0) {
					f++;
					index = 0;
				}

				tokenizer = new StringTokenizer(line, ",");
				if (tokenizer.countTokens() > 1) {
					int label = (int) Float.parseFloat(tokenizer.nextToken());
					if (label > 0)
						labels[f][index] = 1;
					else
						labels[f][index] = -1;

					int index2 = 0;
					while (tokenizer.hasMoreTokens()) {
						features[f][index][index2] = Double.parseDouble(tokenizer.nextToken());
						index2++;
					}
				}
				index++;
			}

			lines.close();
			long estimatedTime = System.nanoTime() - startTime;
			double seconds = (double) estimatedTime / 1000000000.0;
			System.out.printf("[INFO] loadfile_and_split_train->Time elapsed: %.2f seconds\n\n", seconds);

		} catch (FileNotFoundException e) {
			System.out.println("ERROR - SVM.loadfile_and_split_train");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR - SVM.loadfile_and_split_train");
			e.printStackTrace();
		}
	}

	public static double[] train(int sizeTrain, int numFrag, int numDim, String trainFile, int maxIters, double lambda,
			double threshold, double lr) {

		int sizeTrainPerFrag = (int) Math.ceil((float) sizeTrain / numFrag);
		int[][] labels = new int[numFrag][sizeTrainPerFrag];
		double[][][] features = new double[numFrag][sizeTrainPerFrag][numDim];

		loadfile_and_split_train(features, labels, trainFile, sizeTrainPerFrag, sizeTrain); // Only
																							// Master

		double[] w = new double[numDim]; // Array of weights that are assigned
											// to individual samples
		double[][] COST = new double[numFrag][2]; // Cost of each partition
		double[][] grad_p = new double[numFrag][numDim]; // gradient ∇(j) w.r.t.
															// w(j)

		for (int iter = 0; iter < maxIters; iter++) {

			for (int f = 0; f < numFrag; f++)
				grad_p[f] = calc_CostAndGrad(numDim, features[f], labels[f], COST[f], f, lambda, w);

			// Accumulate gradient and Cost
			int size = numFrag;
			int i = 0;
			int gap = 1;
			while (size > 1) {
				accumulateCostAndGrad(grad_p[i], grad_p[i + gap], COST[i], COST[i + gap]);
				size--;
				i = i + 2 * gap;
				if (i == numFrag) {
					gap *= 2;
					i = 0;
				}
			}

			// System.out.println("[INFO] - Current Cost: "+ COST[0][0]);
			// if(COST[0][0]< threshold){
			// System.out.println("[INFO] - Final Cost: "+ COST[0][0]);
			// break;
			// }

			// Step:Update
			updateWeight(lr, grad_p[0], w);
		}

		// labels = null;
		// features = null;
		// grad_p = null;
		// COST = null;
		// System.gc();
		return w;
	}

	public static void main(String[] args) throws IOException {
		// http://snap.stanford.edu/class/cs246-2015/slides/13-svm.pdf

		/*
		 * ########################################### #### Load and set the
		 * environment #### ###########################################
		 */

		// SVM's parameters
		double lambda = 0.001; // Coefficient for Penalty part (regularization
								// parameter) -> 0 to +inf
		double lr = 0.0001; // Learning rate parameter
		double threshold = 0.001; // Cost's threshold -> Tolerance for stopping
									// criterion
		int maxIters = 3;

		// Dataset's parameters
		int numDim = 0;
		int sizeTrain = 0;
		int sizeTest = 0;
		int numFrag = 1;
		String trainFile = "";
		String testFile = "";
		String path_output = "";

		// Get and parse arguments
		int argIndex = 0;
		while (argIndex < args.length) {
			String arg = args[argIndex++];
			if (arg.equals("-c")) {
				lambda = Double.parseDouble(args[argIndex++]);
			} else if (arg.equals("-lr")) {
				lr = Double.parseDouble(args[argIndex++]);
			} else if (arg.equals("-thr")) {
				threshold = Double.parseDouble(args[argIndex++]);
			} else if (arg.equals("-nd")) {
				numDim = Integer.parseInt(args[argIndex++]);
			} else if (arg.equals("-nt")) {
				sizeTrain = Integer.parseInt(args[argIndex++]);
			} else if (arg.equals("-nv")) {
				sizeTest = Integer.parseInt(args[argIndex++]);
			} else if (arg.equals("-i")) {
				maxIters = Integer.parseInt(args[argIndex++]);
			} else if (arg.equals("-f")) {
				numFrag = Integer.parseInt(args[argIndex++]);
			} else if (arg.equals("-t")) {
				trainFile = args[argIndex++];
			} else if (arg.equals("-v")) {
				testFile = args[argIndex++];
			} else if (arg.equals("-out")) {
				path_output = args[argIndex++];
			}
		}
		if (trainFile.equals("") || testFile.equals("")) {
			System.out.println("[ERROR] - You need to choose a file to train and test");
			System.exit(0);
		}

		System.out.println("Running SVM.files with the following parameters:");
		System.out.println("- Lambda: " + lambda);
		System.out.println("- Learning rate: " + lr);
		System.out.println("- Threshold: " + threshold);
		System.out.println("- Iterations: " + maxIters);
		System.out.println("- Dimensions: " + numDim);
		System.out.println("- Nodes: " + numFrag);
		System.out.println("- Train Points: " + trainFile + " - numPoints: " + sizeTrain);
		System.out.println("- Test Points: " + testFile + " - numPoints: " + sizeTest);
		System.out.println("- Output path: " + path_output + "\n");

		/*
		 * ######################################## #### Create Model ####
		 * ########################################
		 */
		// Step: Find the weight vector w that minimizes the expected loss on
		// the training data
		double[] w = train(sizeTrain, numFrag, numDim, trainFile, maxIters, lambda, threshold, lr);

		/*
		 * ######################################## #### Test Model ####
		 * ########################################
		 */

		int sizeTestPerFrag = (int) Math.ceil((float) sizeTest / numFrag);
		double[][][] test_features = new double[numFrag][sizeTestPerFrag][numDim];

		loadfile_and_split_test(test_features, testFile, sizeTestPerFrag, sizeTest);

		for (int f1 = 0; f1 < numFrag; f1++) {
			int[] labels_result = predict_chunck(test_features[f1], w);
			if (!path_output.equals("")) {
				File f = new File(path_output + "_part" + f1);
				f.createNewFile();
				savePredictionToFile(labels_result, path_output + "_part" + f1);
			}
		}

	}

}