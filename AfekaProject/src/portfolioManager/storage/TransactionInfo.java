package portfolioManager.storage;


import java.util.Scanner;

public class TransactionInfo {
	private String stockName;
	private int id;
	private int amount;
	private int price;
	private String kind;

	public TransactionInfo(String stockName, int id, int amount, int price, String kind) {

		this.stockName = stockName;
		this.id = id;
		this.amount = amount;
		this.price = price;
		this.kind = kind;
	}

	public TransactionInfo(Scanner scanner) {
		this.stockName = scanner.next();
		this.id = scanner.nextInt();
		this.amount = scanner.nextInt();
		this.price = scanner.nextInt();
		this.kind = scanner.next();
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

	@Override
	public String toString() {
		return "TransactionInfo [stockName=" + stockName + ", id=" + id + ", amount=" + amount + ", price=" + price
				+ ", kind=" + kind + "]";
	}

}
