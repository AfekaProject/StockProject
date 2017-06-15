package portfolio.storage;

public class portfolioInfo {

	private int numOfSecurities;
	private float yield;
	private float value;
	private float totalProfit;

	public portfolioInfo() {
		this.numOfSecurities = numOfSecurities;
		this.yield = yield;
		this.value = value;
		this.totalProfit = totalProfit;
	}

	public int getNumOfSecurities() {
		return numOfSecurities;
	}

	public void setNumOfSecurities(int numOfSecurities) {
		this.numOfSecurities = numOfSecurities;
	}

	public float getYield() {
		return yield;
	}

	public void setYield(float yield) {
		this.yield = yield;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getTotalProfit() {
		return totalProfit;
	}

	

}
