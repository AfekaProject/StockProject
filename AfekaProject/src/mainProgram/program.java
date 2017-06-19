package mainProgram;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import portfolioManager.iface.portfolioManagerApp;

public class program {

	private static Scanner s = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		String name;
		int password, choice;
		try {
			portfolioManagerApp portfolio = new portfolioManagerApp();
			// tradingManagerApp trade = new tradingManagerApp();
			System.out.println("Please enter User-Name :");
			name = s.nextLine();
			System.out.println("Please enter Password (digits): ");
			password = s.nextInt();

			////////////// login /////////

			System.out.println("\n\nWELCOME " + name);
			System.out.println("\nPlease enter your choice:");
			System.out.println("1- Bid/Ask stocks \n2- Show Portfolio \n3- Log out");
			do {
				choice = s.nextInt();

				// if (choice == 1)
				// trade.run();

				if (choice == 2)
					portfolio.run();
				else if (choice == 3)
					System.exit(0);
				else
					System.out.println("invalid choice.. try again");
			} while (choice < 1 || choice > 3);
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
