package mainProgram;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import portfolioManager.iface.portfolioManagerApp;
import tradingManager.iface.TradingApp;

public class Program {

	private static Scanner s = new Scanner(System.in);
	final static String PASSWORD = "82S9r5";
	final static int ACCOUNT = 108;
	final static String IP = "localhost";
	static portfolioManagerApp portfolio;
	static TradingApp trade;
	public static void main(String[] args) throws IOException {
		int accountId= ACCOUNT;
		String secret=PASSWORD;
		
		try {
			portfolio = new portfolioManagerApp(accountId , secret ,IP);
			trade = new TradingApp(accountId , secret ,IP);
			
			//////////////login /////////
			
			// System.out.println("Please enter User-Name :");
			// accountId = s.nextLine();
			// System.out.println("Please enter Password (digits): ");
			// secret = s.nextInt();

			// System.out.println("\n\nWELCOME " + name);
			System.out.println("\nPlease enter your choice:");
			System.out.println("1- Bid/Ask stocks \n2- Show Portfolio \n3- Log out");
			showMain();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void showMain(){
		int  choice;
		do {
			choice = s.nextInt();

			if (choice == 1)
				trade.run();
			if (choice == 2)
				portfolio.run();
			else if (choice == 3)
				System.exit(0);
			else
				System.out.println("invalid choice.. try again");
		} while (choice < 1 || choice > 3);

	}

}
