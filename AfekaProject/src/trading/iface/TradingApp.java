package trading.iface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import auth.api.WrongSecretException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;

public class TradingApp {
	Scanner scanner;
	ExchangeManager exchange;
	public TradingApp () throws MalformedURLException, RemoteException, NotBoundException{
		scanner = new Scanner(System.in);
		exchange = (ExchangeManager) Naming.lookup("rmi://localhost/Exchange");
		
	}
	public void run() {
		Integer code;
		do {
			showMenu();
			code = selectOperation();
			try {
				switch(code) {
				case 1: bidMenu(); break;
				case 2: askMenu(); break;
				case 3: System.exit(0);
				default: 
					System.out.println("Invalid choice.");
				}
			}catch(Exception err) {
				System.out.println("Error: " + err.getMessage());
			}	
		}while (code !=3);
		
	}
	
	private void showMenu() {
		System.out.println("1 - Bid\n"
				+ "2 - Ask\n"
				+ "3 - Back to main menu/");
	}
	
	private Integer selectOperation() {
		Integer code = scanner.nextInt();
		while (code < 1 || code > 3)
		{
			System.out.println("Invalid choice. Try again (1-3)");
			code = scanner.nextInt();
		}
		return code;
	}


	private void bidMenu() throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException{
		System.out.println("Enter stock name:");
		String name = scanner.nextLine();
		Set<String> stockList = exchange.getStockNames();
		boolean foundStock = stockList.contains(name); 
		if (foundStock){
			List<Integer> orders = exchange.getOpenOrders("1", 1);
			
		}
			
	}
	
	private void askMenu(){
		
	}
}
