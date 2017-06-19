package portfolioManager.logic;

import java.util.Date;

public class Transaction {
	private int id;
	private Date startDate;
	private Date endDate;
	private String status;
	private String name;

	public Transaction(String name, int id, Date startDate, Date endDate, String status) {
		this.name = name;
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getName() {
		return name;
	}

}
