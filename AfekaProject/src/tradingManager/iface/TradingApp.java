package tradingManager.iface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.InternalServerErrorException;
import exchange.api.DoesNotHaveThisStockException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughStockException;
import exchange.api.Order;
import exchange.api.StockNotTradedException;
import tradingManager.logic.AskBid;

public class TradingApp {
	Scanner s;
	ExchangeManager exchange;
	BankManager bankManager;
	private static final String SECRET = "82S9r5";
	private static final int ACCOUNTID = 108;

	public TradingApp() throws MalformedURLException, RemoteException, NotBoundException {
		s = new Scanner(System.in);
		exchange = (ExchangeManager) Naming.lookup("rmi://172.20.17.99/Exchange");
		bankManager = (BankManager) Naming.lookup("rmi://172.20.17.99/Bank");

	}

	public void run() {
		int code;
		AskBid.initAccount(ACCOUNTID, SECRET);
		do {
			showMenu();
			code = selectOperation(1, 3);
			try {
				switch (code) {
				case 1:
					bidMenu();
					break;
				case 2:
					askMenu();
					break;
				case 3:
					System.exit(0);
				default:
					System.out.println("Invalid choice.");
				}
			} catch (Exception err) {
				System.out.println("Error: " + err.getMessage());
			}
		} while (code != 3);

	}

	private void showMenu() {
		System.out.println("1 - Bid\n" + "2 - Ask\n" + "3 - Back to main menu/");
	}

	private Integer selectOperation(int start, int end) {
		Integer code = s.nextInt();
		while (code < start || code > end) {
			System.out.println("Invalid choice. Try again (" + start + "-" + end + ")");
			code = s.nextInt();
		}
		return code;
	}

	private void bidMenu() throws RemoteException, NoSuchAccountException, WrongSecretException,
			InternalExchangeErrorException, NotEnoughStockException, StockNotTradedException {
		int operation;
		System.out.println("Enter stock name:");
		String name = s.nextLine();
		boolean found = AskBid.stockSearch(name);
		if (found) {
			AskBid.printStockInfo(name);
			System.out.println("\n Do you want to place a bid? 1-Yes , 2- try another search , 3-back to menu");
			operation = selectOperation(1, 3);

			if (operation == 1) {
				System.out.println("Please enter amount and price for the bid");
				int amount = s.nextInt();
				int price = s.nextInt();
				int bidId = AskBid.placeBid(name, amount, price);
				if (bidId == -1) {// the trader doesn't have enough money, the bid failed
					System.out.println("Sorry, you don't have enough money");

				} else {
					System.out.println("Your bid id is: " + bidId);
				}

			} else if (operation == 2){
				bidMenu();
			}else if (operation == 3){
				run();
			}
		

		
			
		} else { // stock wasnt found
			System.out.println("Stock wasn't found 1-search again , 2- main ");
			operation = selectOperation(1, 2);
			
			if(operation == 1)
				bidMenu();
			else if(operation == 2)
				run();

		}
	

	

	}

	private void askMenu() throws RemoteException, WrongSecretException, InternalServerErrorException,
			NoSuchAccountException, InternalExchangeErrorException, NotEnoughStockException, StockNotTradedException,
			DoesNotHaveThisStockException {
		Set<String> myAssets = bankManager.getAssets("1", 1);

		System.out.println("Your assets:\n");

		for (String assetName : myAssets) {
			System.out.println(myAssets);
		}
		System.out.println("Please enter the name of the asset you would like to ask");
		String assetNameToAsk = s.nextLine();

		List<Integer> orders = exchange.getOpenOrders("1", 1); // Give list of
																// pending ask
																// id

		for (Integer orderId : orders) {
			Order tempOrder = exchange.getOrderDetails("1", 1, orders.get(orderId)); // Get
																						// the
																						// name
																						// of
																						// the
																						// order
			if (tempOrder.getStockName().compareToIgnoreCase(assetNameToAsk) > 0
					&& tempOrder.getKind().compareTo("A") > 0) {// If name found
																// on pending
																// list
				System.out.println("There is already a pending ask for that stock\n What Would you Like to do?");
				System.out.println("1 - Update\n" + "2 - Back");

				int operation = selectOperation(1, 2);
				if (operation == 1) { // Update
					printOrderInfo(tempOrder);
					updateOrPlaceAsk(tempOrder.getStockName(), 1);
					run();
				}

				else if (operation == 2) { // Back or search again
					System.out.println(
							"the stock you requested wasnt found. \n Press 1 for new search \nPress 2 for main menu");

					int selection = selectOperation(1, 2);
					if (selection == 1)
						askMenu();
					else
						run();
				}
			} else {
				updateOrPlaceAsk(assetNameToAsk, 1);
			}

		}
	}

	private void updateOrPlaceAsk(String name, int accountId)
			throws RemoteException, WrongSecretException, NoSuchAccountException, NotEnoughStockException,
			StockNotTradedException, DoesNotHaveThisStockException, InternalExchangeErrorException {
		System.out.println("Please enter amount");
		int amount = s.nextInt();
		System.out.println("Please enter price");
		int price = s.nextInt();

		/*
		 * Need to put here a check if user have enough money in back if(User
		 * doesnt have enough money in back){
		 * System.out.println("There is not enough money to make this bid");
		 * update(order,accountId); }
		 * 
		 * else{ //user have enough money }
		 */

		int newAskId = exchange.placeAsk("1", 1, name, amount, price);
		System.out.println("Ask has been placed, the Ask ID is: " + newAskId);
	}

}
