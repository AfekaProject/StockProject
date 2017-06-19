package tradingManager.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import tradingManager.logic.AskBid;


public class TradingStorage {
	private File file;
	
	public TradingStorage() throws IOException {
		file = new File("portfolio.data");
	
		if (!file.exists())
			Files.createFile(Paths.get("portfolio.date"));
	}
	
	public void store (AskBid order){
		List<AskBidInfo> orderList;
		orderList=load();
		orderList.add(new AskBidInfo (order.getStockName(),order.getId()
				,order.getAmount(),order.getPrice(),order.getKind()));
		
		PrintStream out = null;
		try {
			out = new PrintStream (file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(AskBidInfo info : orderList) {
			info.writeInto(out);
		}
	}
	
	public List<AskBidInfo> load (){
		List<AskBidInfo> orderList = new LinkedList<AskBidInfo>();
		Scanner scanner = null;
		try {
			scanner = new Scanner (new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//scanner.useDelimiter("\\n|,");
		while (scanner.hasNext()){
			orderList.add(new AskBidInfo (scanner));
		}
		return orderList;
	}
	
}
