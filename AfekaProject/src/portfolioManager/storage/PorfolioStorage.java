package portfolioManager.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PorfolioStorage {

	private File file;

	public PorfolioStorage() throws IOException {
		file = new File("DB.txt");
		if (!file.exists())
			Files.createFile(Paths.get("DB.txt"));
	}

	public List<TransactionInfo> load() throws FileNotFoundException { ///////change get transactions
		List<TransactionInfo> list = new LinkedList<TransactionInfo>();
		Scanner scanner = new Scanner(new FileInputStream(file));
		// scanner.useDelimiter("\\n|,");
		while (scanner.hasNext()) {
			list.add(new TransactionInfo(scanner));
		}
		scanner.close();
		return list;
	}

}
