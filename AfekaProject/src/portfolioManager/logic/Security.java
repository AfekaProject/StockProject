package portfolioManager.logic;


public class Security {

	private String name;
	private int quantity;

	public Security(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Security [name=" + name + ",  quantity=" + quantity + "]";
	}

}
