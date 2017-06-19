package portfolioManager.iface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import auth.api.WrongSecretException;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import portfolioManager.logic.Portfolio;

public class portfolioManagerApp {
	final String PASSWORD = "82S9r5";
	final int ACCOUNT = 108;
	private static Scanner s = new Scanner(System.in);
	private Portfolio portfolio;

	public portfolioManagerApp() throws IOException, NotBoundException, NoSuchAccountException, WrongSecretException,
			InternalExchangeErrorException {
		portfolio = new Portfolio(PASSWORD, ACCOUNT);
	}

	public void run() {
		showMenu();
		Integer code = selectOperation();

		while (code != 4) {
			try {
				switch (code) {
				case 1: {
					showAllSecurities();
					break;
				}
				case 2: {
					showHistory();
					break;
				}
				case 3: {
					System.exit(0);
				}
				default:
					System.out.println("Invalid choice.");
				}
			} catch (Exception err) {
				System.out.println("Error: " + err.getMessage());
			}
			showMenu();
			code = selectOperation();
		}
	}

	private void showHistory() throws FileNotFoundException {
		portfolio.history();
	}

	private void showAllSecurities()
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException {
		portfolio.show();
	}

	private void showMenu() {
		System.out.println("1 - view all securties \n2 - view history\n3 - Exit.");
	}

	private Integer selectOperation() {
		Integer code = s.nextInt();
		while (code < 1 || code > 3) {
			System.out.println("Invalid choice. Try again (1-4)");
			code = s.nextInt();
		}
		return code;
	}

}
