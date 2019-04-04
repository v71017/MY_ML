package salimm.benchmark.objs;

/**
 * 
 * 
 * 
 * @author salimm
 *
 */
public class BenchmarkData {
	private String name;
	private int totalNumberOfClasses;

	public BenchmarkData(String name,int totalNumberOfClasses) {
		this.setTotalNumberOfClasses(totalNumberOfClasses);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getTotalNumberOfClasses() {
		return totalNumberOfClasses;
	}

	public void setTotalNumberOfClasses(int totalNumberOfClasses) {
		this.totalNumberOfClasses = totalNumberOfClasses;
	}
}
