package portfolioManager.logic;

public class Transaction {
	private String stockName;
	private int id;
	private int amount;
	private int price;
	private String kind;

	public Transaction(String stockName, int id, int amount, int price, String kind) {

		this.stockName = stockName;
		this.id = id;
		this.amount = amount;
		this.price = price;
		this.kind = kind;
	}

	public String getStockName() {
		return stockName;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public int getPrice() {
		return price;
	}

	public String getKind() {
		return kind;
	}

}
