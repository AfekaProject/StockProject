package tradingManager.logic;

import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.*;
import tradingManager.storage.TradingStorage;

import java.net.MalformedURLException;
import java.rmi.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import auth.api.WrongSecretException;

public class AskBid {
	private String stockName;
	private int id;
	private int amount;
	private int price;
	private String kind;
	private static int accountId;
	private static String secret;
	private static ExchangeManager exchange;
	private static BankManager bankManager;
	private static TradingStorage storage;
	private static final int STOCK_EXCHANGE_ID = 3373;

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

	public AskBid(String stockName, int amount, int price, String kind) {
		this.stockName = stockName;
		this.amount = amount;
		this.price = price;
		this.kind = kind;

	}

	public static void initAccount(int accountIdTemp, String secretTemp) {
		accountId = accountIdTemp;
		secret = secretTemp;
	}

	public static void initConnect(String ip) {
		try {
			exchange = (ExchangeManager) Naming.lookup("rmi://"+ ip +"/Exchange");
			bankManager = (BankManager) Naming.lookup("rmi://"+ ip +"/Bank");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Boolean stockSearch(String name) throws RemoteException {
		
		Set<String> stockList = exchange.getStockNames();

		boolean foundStock = stockList.contains(name);

		return foundStock;
	}

	public static Order orderSearch(String name, String kind)
			throws RemoteException, WrongSecretException, NoSuchAccountException, InternalExchangeErrorException {

		List<Integer> orders = exchange.getOpenOrders(secret, accountId);

		for (int i = 0; i < orders.size(); i++) {
			// for (Integer orderId : orders) {
			Order tempOrder = exchange.getOrderDetails(secret, accountId, orders.get(i));
			if ((tempOrder.getStockName().compareToIgnoreCase(name) != 0)
					&& (tempOrder.getKind().compareTo(kind) != 0)) {
				return tempOrder;

			}

		}
		return null;
	}

	public static Order bidSearch(String name)
			throws RemoteException, WrongSecretException, NoSuchAccountException, InternalExchangeErrorException {

		List<Integer> orders = exchange.getOpenOrders(secret, accountId); //// Give
																			//// list
																			//// of
																			//// pending
																			//// list
		for (Integer orderId : orders) {
			Order tempOrder = exchange.getOrderDetails(secret, accountId, orders.get(orderId));
			if (tempOrder.getStockName().compareToIgnoreCase(name) > 0 && tempOrder.getKind().compareTo("B") > 0) {
				return tempOrder;

			}

		}
		return null;
	}

	public static int placeBid(String name, int amount, int price)
			throws RemoteException, WrongSecretException, DoesNotHaveThisAssetException, InternalServerErrorException,
			NotEnoughAssetException, NoSuchAccountException, InternalExchangeErrorException, NotEnoughStockException,
			StockNotTradedException, InterruptedException {
		int total = amount * price;
		int accountMoney = bankManager.getQuantityOfAsset(secret, accountId, "NIS");

		if (total <= accountMoney) { // check if have enough money
			bankManager.transferAssets(secret, accountId, STOCK_EXCHANGE_ID, "NIS", amount);
			java.util.concurrent.TimeUnit.SECONDS.sleep(2);
			int bidId = exchange.placeBid(secret, accountId, name, amount, price);
			AskBid tempBid = new AskBid(name, amount, price, "B");
			storage.store(tempBid);
			return bidId;
		} else { // the trader don't have enough money
			return -1;
		}
	}

	public static void printOrderInfo(Order myOrder) {
		System.out.println("The bid information:\n Stock Name: " + myOrder.getStockName() + "\nCreation Date: "
				+ myOrder.getCreationDate() + "\nStock Price: " + myOrder.getPrice() + "\nAmount: "
				+ myOrder.getAmount());
		if (myOrder.getKind().compareTo("A") > 0)
			System.out.println("Order kind: Ask");
		else
			System.out.println("Order kind: Bid");
	}

	public static void printStockInfo(String name) throws RemoteException {
		System.out.println("The stock information:\nStock name: " + name);
		Map<Integer, Integer> supply = exchange.getSupply(name);
		Map<Integer, Integer> demand = exchange.getDemand(name);
		int counter = 1;

		System.out.println("\nSupply:");
		for (Entry<Integer, Integer> entry : supply.entrySet()) {
			System.out.println((counter++) + " Price: " + entry.getKey() + " | Amount: " + entry.getValue());

		}
		System.out.println("\nDemand:");
		for (Entry<Integer, Integer> entry : demand.entrySet()) {
			System.out.println((counter++) + " Price: " + entry.getKey() + " | Amount: " + entry.getValue());

		}
	}

	public static void printAssetsInfo()
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException, DoesNotHaveThisAssetException, InternalServerErrorException {
		int counter = 1;
		Set<String> allAssets = exchange.getAssets(secret, accountId);
		if (allAssets.isEmpty())
			System.out.println("You don't have assets");
		else
			for (String nameOfAsset : allAssets)
				System.out.println((counter++)+ ".\t" + nameOfAsset + "  -  " + bankManager.getQuantityOfAsset(secret, accountId, nameOfAsset));

	}

	public static int placeAsk(String name, int amount, int price) throws NoSuchAccountException,
			NotEnoughStockException, StockNotTradedException, DoesNotHaveThisStockException,
			InternalExchangeErrorException, RemoteException, WrongSecretException, InternalServerErrorException {

		boolean exist = bankManager.getAssets(secret, accountId).contains(name.toUpperCase());

		if (exist) {
			int askId = exchange.placeAsk(secret, accountId, name, amount, price);
			AskBid tempAsk = new  AskBid(name, amount, price, "A");
			storage.store(tempAsk);
			return askId;
		} else {
			return -1;
		}

	}

}
