package portfolioManager.storage;

import java.util.Date;
import java.util.Scanner;

public class TransactionInfo {
	private int id;
	private Date startDate;
	private Date endDate;
	private String status;
	private String name;

	public TransactionInfo(String name, int id, Date startDate, Date endDate, String status) {
		this.name = name;
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public TransactionInfo(Scanner scanner) {
		this.name = scanner.next();
		this.id = scanner.nextInt();
		// this.startDate= scanner;
		// this.endDate=scanner
		this.status = scanner.next();
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

	public String getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "TransactionInfo [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status
				+ ", name=" + name + "]";
	}

}
