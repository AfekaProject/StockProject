package portfolio.logic;

public class Account {
	private Portfolio portfolio;
	private int id;
	private String name;
	private String password;

	public Account(String password, String name, int id) {
		this.password = password;
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static boolean login(String name, String password) {
		// check in the storage portfolio for the name and pass
		return true;

	}
	
	public Portfolio getPortfolio(){
		return portfolio;
	}

}
