import weka.core.Attribute;

public class Split 
{
	public Attribute attribute;
	public Double threshold;
	
	public Split(Attribute attribute) {
		this.attribute = attribute;
		threshold = null;
	}
	
	public Split(Attribute attribute, Double threshold) {
		this.attribute = attribute;
		this.threshold = threshold;
	}
}