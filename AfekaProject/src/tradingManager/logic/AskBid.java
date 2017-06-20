package tradingManager.logic;

import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.*;
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

	public static void initConnect() {
		try {
			exchange = (ExchangeManager) Naming.lookup("rmi://172.20.17.99/Exchange");
			bankManager = (BankManager) Naming.lookup("rmi://172.20.17.99/Bank");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Boolean stockSearch(String name) throws RemoteException {
		initConnect();
		Set<String> stockList = exchange.getStockNames();

		boolean foundStock = stockList.contains(name);

		return foundStock;
	}


	public static Order orderSearch(String name, String kind)
			throws RemoteException, WrongSecretException, NoSuchAccountException, InternalExchangeErrorException {

		List<Integer> orders = exchange.getOpenOrders(secret, accountId); 
		
		for (Integer orderId : orders) {
			Order tempOrder = exchange.getOrderDetails(secret, accountId, orders.get(orderId));
			if (tempOrder.getStockName().compareToIgnoreCase(name) > 0 && tempOrder.getKind().compareTo(kind) > 0) {
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

	public static int placeBid(String name, int amount, int price) throws RemoteException, WrongSecretException,
			DoesNotHaveThisAssetException, InternalServerErrorException, NotEnoughAssetException, NoSuchAccountException, InternalExchangeErrorException, NotEnoughStockException, StockNotTradedException {
		int total = amount * price;
		int accountMoney = bankManager.getQuantityOfAsset(name, accountId, "NIS");

		if (total <= accountMoney) { //check if have enough money
			bankManager.transferAssets(secret, accountId, STOCK_EXCHANGE_ID, name, amount);
			int bidId = exchange.placeBid(name, accountId, name, amount, price);
			return bidId;
		} else { // the trader don't have enough money
			return -1 ;
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
	
	public static void printStockInfo(String name) throws RemoteException{
		System.out.println("The stock information:\n Stock name: \n"+ name );
		Map <Integer,Integer> supply = exchange.getSupply(name);
		int counter=1;
		
		for(Entry <Integer,Integer> entry : supply.entrySet()){
			System.out.println(counter + " Price: " + entry.getKey() + " | Amount: " + entry.getValue());
			
		}
	}


	public static void printAssetsInfo()
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException {
		int counter = 1;
		Set<String> allAssets = exchange.getAssets(secret, accountId);

		for (String nameOfAsset : allAssets) {
			System.out.println((counter++) + nameOfAsset);

		}

	}
	
	public static int placeAsk(String name, int amount, int price) throws NoSuchAccountException,
			NotEnoughStockException, StockNotTradedException, DoesNotHaveThisStockException,
			InternalExchangeErrorException, RemoteException, WrongSecretException, InternalServerErrorException {
		
		boolean exist = bankManager.getAssets(name, accountId).contains(name.toLowerCase());

		if (exist) {
			int askId = exchange.placeAsk(name, accountId, name, amount, price);
			return askId;
		} else {
			return -1;
		}

	}
	
}
