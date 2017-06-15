package portfolio.iface;

import java.io.IOException;
import java.util.Scanner;

import portfolio.logic.Account;
import portfolio.logic.Portfolio;

public class portfolioManagerApp {
	private String name;
	private String password;
	private boolean flag;
	private int i = 3, res;
	private static Scanner s = new Scanner(System.in);

	public static void main(String[] args) {
		(new portfolioManagerApp()).run();
	}

	private void run() {
		System.out.println("Please enter user- name :");
		name = s.nextLine();
		System.out.println("Please enter password: ");
		password = s.nextLine();
		flag = Account.login(name, password);
		while (!flag) {
			if (i == 0)
				System.exit(0);
			System.out.println("wrong user name and password, try again ( " + i + " tries remaining) ");
			System.out.println("Please enter user- name :");
			name = s.nextLine();
			System.out.println("Please enter password: ");
			password = s.nextLine();
			flag = Account.login(name, password);
			i--;
		}
		System.out.println("WELCOME " + name.toUpperCase());
		showMenu();
		Integer code = selectOperation();

		while (code != 4) {
			try {
				switch (code) {
				case 1:
					buy();
					break;
				case 2:
					sell();
					break;
				case 3:
					watch();
					break;
				case 4:
					System.exit(0);
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

	private void showMenu() {
		System.out.println("1 - Buy Stock\n2 - Sell Stock\n3 - Watch Portfolio\n4 - Exit.");
	}

	private Integer selectOperation() {
		Integer code = s.nextInt();
		while (code < 1 || code > 4) {
			System.out.println("Invalid choice. Try again (1-4)");
			code = s.nextInt();
		}
		return code;
	}

	private void watch() {
		Portfolio portfolio = Account.getPortfolio();
		System.out.println("1 - Watch all the stocks you hold\n2 - Watch the yield\n3 - Watch total profit");
		res = s.nextInt();
		while (res < 1 || res > 3) {
			System.out.println("Invalid choice. Try again (1-3)");
			res = s.nextInt();
		}
		if (res == 1)
			System.out.println("Owned stocks : " + Portfolio.getStocks());
		else if (res == 2)
			System.out.println("Yield : " + Portfolio.getYield());
		else if (res == 3)
			System.out.println("Profit: " + name + " is " + Portfolio.getProfit());

	}

	private void sell() {
		System.out.print("Enter name: ");
		String name = scanner.next();
		try {
			pbook.removeContact(name);
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Sorry, this name does not exist in the phone book.");
		}
	}

	private void buy() throws Exception {
		System.out.print("Enter name: ");
		String name = scanner.next();
		System.out.print("Enter phone number: ");
		String phoneNumber = scanner.next();
		try {
			Account.addNewContact(phoneNumber, name);
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Sorry, this name is already in the phone book.");
		}
	}

}
