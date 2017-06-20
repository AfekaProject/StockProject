package tradingManager.iface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
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
			InternalExchangeErrorException, NotEnoughStockException, StockNotTradedException,
		 InternalServerErrorException, DoesNotHaveThisAssetException, NotEnoughAssetException {
		int operation;
		System.out.println("Enter stock name:");
		String name = s.nextLine();
		boolean found = AskBid.stockSearch(name); // search for the stock by
													// name

		if (found) { // stock found
			AskBid.printStockInfo(name); // print info about the stock
			System.out.println("\n Do you want to place a bid? 1-Yes , 2- try another search , 3-back to menu");
			operation = selectOperation(1, 3);

			if (operation == 1) {
				Order pendingOrder = AskBid.orderSearch(name, "B");
				if (pendingOrder != null) { // already have a bid for this stock
					System.out.println("You have an open bid for this stock: \n");
					AskBid.printOrderInfo(pendingOrder);
				} else {
					System.out.println("Please enter amount and price for the bid");
					int amount = s.nextInt();
					int price = s.nextInt();
					int bidId = AskBid.placeBid(name, amount, price);
					if (bidId == -1) { // the trader doesn't have enough money,
										// the bid failed
						System.err.println("Sorry, you don't have enough money for this bid");

					} else {
						System.out.println("Bid has been placed, Your bid id is: " + bidId); // bid
																								// done
					}
				}
			} else if (operation == 2) {
				bidMenu();
			} else if (operation == 3) {
				run();
			}

		} else { // stock wasn't found
			System.out.println("Stock wasn't found 1-search again , 2- main ");
			operation = selectOperation(1, 2);

			if (operation == 1)
				bidMenu();
			else if (operation == 2)
				run();

		}

	}

	private void askMenu() throws RemoteException, WrongSecretException, InternalServerErrorException,
			NoSuchAccountException, InternalExchangeErrorException, NotEnoughStockException, StockNotTradedException,
			DoesNotHaveThisStockException {
		int operation;

		System.out.println("Your assets:\n");
		AskBid.printAssetsInfo();

		System.out.println("Please enter the name of the asset you would like to ask");
		String assetNameToAsk = s.nextLine();

		AskBid.printStockInfo(assetNameToAsk);
		System.out.println("Do you want to place an ask for this stock? 1- yes , 2- another search, 3-main");
		operation = selectOperation(1, 3);
		
		if (operation == 1) { // place an ask
			Order pendingAsk = AskBid.orderSearch(assetNameToAsk, "A");
			if (pendingAsk != null) { // ask for this stock is not exist
				System.out.println("enter amount and price for : " + assetNameToAsk.toUpperCase());
				int amount = s.nextInt();
				int price = s.nextInt();

				int askId = AskBid.placeAsk(assetNameToAsk, amount, price);

				if (askId == -1) {
					System.err.println("ASK FAILED");
				} else {
					System.out.println("Ask has been placed, Your ask id is: " + askId);
				}

			} else { // ask for this stock is already exist
				System.out.println("You have an open ask for this stock: \n");
				AskBid.printOrderInfo(pendingAsk);
			}
		} else if (operation == 2) {
			askMenu();
		} else if (operation == 3) {
			run();
		}
	}
}
