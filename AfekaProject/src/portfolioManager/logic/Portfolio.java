package portfolioManager.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import auth.api.WrongSecretException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import portfolioManager.storage.PorfolioStorage;
import portfolioManager.storage.TransactionInfo;

public class Portfolio {
	private String secret;
	private int accountId;
	private ExchangeManager exchange;
	private Set<Security> allSecurities;
	private List<TransactionInfo> allTransaction;
	private PorfolioStorage storage;

	public Portfolio(String secret, int accountId) throws IOException, NotBoundException, NoSuchAccountException,
			WrongSecretException, InternalExchangeErrorException {
		this.secret = secret;
		this.accountId = accountId;
		exchange = (ExchangeManager) Naming.lookup("rmi://localhost/Exchange");
		setSecurities(exchange.getAssets(secret, accountId));
		storage = new PorfolioStorage();
	}

	private void setSecurities(Set<String> assets)
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException {
		for (String sec : assets)
			allSecurities.add(new Security(sec, exchange.getAmountOfAsset(secret, accountId, sec)));
	}

	public void show()
			throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException {
		for (Security sec : allSecurities)
			System.out.println(sec.toString());
	}

	public void history() throws FileNotFoundException {
		allTransaction = storage.load();
		for (TransactionInfo ti : allTransaction) {
			System.out.println(ti.toString());

		}
	}

}
