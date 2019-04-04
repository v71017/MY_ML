
public class LinearRegressionTest {

	public static void main(String args[]) {

		double[] x = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 };
		double[] y = { 2.0, 1.0, 3.0, 6.0, 9.0, 11.0, 13.0 };

		LinearRegression lr = new LinearRegression(x, y);
		System.out.println(" LR Prediction value is " + lr.predict(6.0));

	}
}
