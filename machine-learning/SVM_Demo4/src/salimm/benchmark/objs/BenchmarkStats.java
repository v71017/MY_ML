package salimm.benchmark.objs;

public class BenchmarkStats {

	private double accuracy;

	private double precision;

	private double aucROC;

	private double aucPR;

	private double confusionMatrix;

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getAucROC() {
		return aucROC;
	}

	public void setAucROC(double aucROC) {
		this.aucROC = aucROC;
	}

	public double getAucPR() {
		return aucPR;
	}

	public void setAucPR(double aucPR) {
		this.aucPR = aucPR;
	}

	public double getConfusionMatrix() {
		return confusionMatrix;
	}

	public void setConfusionMatrix(double confusionMatrix) {
		this.confusionMatrix = confusionMatrix;
	}

}
