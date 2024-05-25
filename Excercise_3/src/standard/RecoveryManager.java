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
	private File directory;
	private Connection connection;
	
	public RecoveryManager(Connection c) {
		directory = new File("src/files");
		transactions = new ArrayList();
		sortedTransactions = new ArrayList();
		connection = c;
	}

	public void recover() {
		readIn();
		sortTransactions();
		commitTransactions();
	}
	
	private void commitTransactions() {
		
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(String line : sortedTransactions)
		{
			try
			{
				String SQL = line.substring(line.indexOf(";") + 2, line.length());
				Statement stmt = connection.createStatement();
	            stmt.execute(SQL);
			}
			catch (Exception e)
			{
				System.out.println(e);
			}
		}
		
		try {
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

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
