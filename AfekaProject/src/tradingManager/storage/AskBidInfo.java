package tradingManager.storage;

import java.io.PrintStream;
import java.util.Scanner;

public class AskBidInfo {
	private String stockName;
	private int id;
	private int amount;
	private int price;
	private String kind;
	
	public AskBidInfo(String stockName, int id, int amount, int price, String kind) {
		setStockName(stockName);
		setId(id);
		setAmount(amount);
		setPrice(price);
		setKind(kind);
	}
	
	public AskBidInfo (Scanner scanner){
		this.stockName = scanner.next();
		this.id = scanner.nextInt();
		this.amount = scanner.nextInt();
		this.price = scanner.nextInt();
		this.kind = scanner.nextLine();
	}

	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public void writeInto (PrintStream out){
		out.println(stockName + " " + id + " " + amount
				+ " " + price + " " + kind);
		
	}
}
