package portfolioManager.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import portfolioManager.storage.PorfolioStorage;
import portfolioManager.storage.TransactionInfo;

public class Portfolio {
	private String secret;
	private int accountId;
	private ExchangeManager exchange;
	private BankManager bankManager;
	private Set<Security> allSecurities;
	private List<TransactionInfo> allTransaction;
	private List<Integer> pendingOrders;
	private PorfolioStorage storage;

	public Portfolio(String secret, int accountId,String ip) throws IOException, NotBoundException, NoSuchAccountException,
			WrongSecretException, InternalExchangeErrorException {
		this.secret = secret;
		this.accountId = accountId;
		initConnect(ip);
		 setSecurities(exchange.getAssets(secret, accountId));
		storage = new PorfolioStorage();
	}

	public void initAccount(int accountIdTemp, String secretTemp) {
		this.accountId = accountIdTemp;
		this.secret = secretTemp;
	}
	public void initConnect(String ip) {
		try {
			exchange = (ExchangeManager) Naming.lookup("rmi://"+ ip +"/Exchange");
			bankManager = (BankManager) Naming.lookup("rmi://"+ ip +"/Bank");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	private void setSecurities(Set<String> assets)
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException {
		for (String sec : assets)
			allSecurities.add(new Security(sec, exchange.getAmountOfAsset(secret, accountId, sec)));
	}

	public void show()
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException, DoesNotHaveThisAssetException, InternalServerErrorException {
//		for (Security sec : allSecurities)
//			System.out.println(sec.toString());
		
		Set<String> allAssets = exchange.getAssets(secret, accountId);
		if (allAssets.isEmpty())
			System.out.println("You don't have assets");
		else
			for (String nameOfAsset : allAssets)
				System.out.println(nameOfAsset + "  -  " + bankManager.getQuantityOfAsset(secret, accountId, nameOfAsset));
	}

	public void history() throws FileNotFoundException, RemoteException, NoSuchAccountException, WrongSecretException,
			InternalExchangeErrorException {
		pendingOrders = exchange.getOpenOrders(secret, accountId);
		allTransaction = storage.load();

		for (int i = 0; i < allTransaction.size(); i++) {
			for (int j = 0; j < pendingOrders.size(); j++) {
				if (allTransaction.get(i).getId() == pendingOrders.get(j))
					allTransaction.remove(i);
			}
		}

		for (int i = 0; i < allTransaction.size(); i++)
			System.out.println(allTransaction.get(i).toString());

	}

}
