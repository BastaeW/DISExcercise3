package standard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecoveryManager {

	private List<String> transactions;
	private List<String> sortedTransactions;
	private Set<Integer> winnerTransactions; // Set of winner transaction IDs
	private File directory;
	private Connection connection;
	
	public RecoveryManager(Connection c) {
		directory = new File("src/files");
		transactions = new ArrayList();
		sortedTransactions = new ArrayList();
		winnerTransactions = new HashSet<>(); // Initialize the winner transactions set
		connection = c;
	}

	public void recover() {
		readIn();
		identifyWinnerTransactions(); // Identify winner transactions from logs
		sortTransactions();
		commitTransactions();
	}
	
	 // Identify winner transactions by parsing the log files
    private void identifyWinnerTransactions() {
        for (String line : transactions) {
            if (line.contains("EOT")) {
                String[] parts = line.split(", ");
                int transactionId = Integer.parseInt(parts[1]);
                winnerTransactions.add(transactionId);
            }
        }
    }
	
 // Commit transactions by redoing the write operations of winner transactions
    private void commitTransactions() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		for (String line : sortedTransactions) {
            try {
                String[] parts = line.split(", ");
                int transactionId = Integer.parseInt(parts[1]);

                // Only redo operations for winner transactions
                if (winnerTransactions.contains(transactionId)) {
			
				String SQL = line.substring(line.indexOf(";") + 2, line.length());
				Statement stmt = connection.createStatement();
	            stmt.execute(SQL);
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}
		}
		
		try {
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
// sort transactions to ensure they are executed in the correct order
	private void sortTransactions() {
		
		String SQL = "";
		for(int i=0; i < transactions.size(); i++)
		{
			for(String line : transactions)
			{
				if(line.substring(0, line.indexOf(",")).equals(String.valueOf(i)))
				{
					SQL = i + "; " + line.substring(line.indexOf(";") + 2, line.length());
					break;
				};
			}
			sortedTransactions.add(SQL);
		}
		
		for (String line : sortedTransactions) {
            System.out.println(line);
        }
	}

	// Read log files to gather transaction data
	private void readIn()
	{
		File[] files = directory.listFiles();
		
		for(File file : files)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				while((line = reader.readLine()) != null)
				{
					transactions.add(line);
				
				}
				reader.close();
			}
			catch(Exception e)
			{
				System.out.print(e);
			}
		}
		
		for (String line : transactions) {
            System.out.println(line);
        }
	}

}
