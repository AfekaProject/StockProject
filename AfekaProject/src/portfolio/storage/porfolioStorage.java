package portfolio.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import pbook.storage.ContactDetailsInfo;


public class porfolioStorage {

	private File file;
	
	public porfolioStorage() throws IOException {
		file = new File("portfolio.data");
		
		if (!file.exists())
			Files.createFile(Paths.get("portfolio.date"));
		
	}
	
	public void store(List<ContactDetailsInfo> list) throws FileNotFoundException {
		PrintStream out = new PrintStream(file);
		for(ContactDetailsInfo info : list) {
			info.writeInto(out);
		}
	}
	
	
	
}
