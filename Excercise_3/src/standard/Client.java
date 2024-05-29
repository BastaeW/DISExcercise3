package standard;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;


public class Client {
	
	// Unique identifier for the client
	private int clientID;
	private PersistenceManager pManager;
	private Connection conn;
	
	public Client(int clientIdentifier, PersistenceManager persistenceManager, Connection connection)
	{
		setClientID(clientIdentifier);
		setpManager(persistenceManager);
		setConn(connection);
		
		// start client operations
		run();
	}
	// simulate client operations
	public void run()
	{
		Random rand = new Random();
		try {
			Thread.sleep(rand.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// perform a random number of transactions
		int randTransactions = rand.nextInt(5) + 1;
		for(int j = 0; j <= randTransactions; j++)
		{
			Integer tr = null;
			tr = pManager.BeginnTransaction(clientID);
			int randWrites = rand.nextInt(5) + 1;
			
			// perform a random number of write operations
			for(int i = 0; i <= randWrites; i++)
			{
				
				int randTable = rand.nextInt(2) + 1;
				int randRow = rand.nextInt(10) + 1;
				int randValue = rand.nextInt(1000);
			
				int randSleep = 1 + (1000*clientID);
				String SQL = "UPDATE vsisp68.\"ex4table" + randTable + "\" SET" + " \"counter\" = '" + randValue + "' WHERE \"row\" = " + randRow;
				pManager.addToBuffer(clientID, SQL, conn, tr);
				/*try {
					conn.setAutoCommit(false);
					
					Statement stmt = conn.createStatement();
		            stmt.execute(SQL);
		            Thread.sleep(randSleep);
				}
				catch(Exception e)
				{
					System.out.println("war nicht ausführbar!");
				}*/
			}
			
			pManager.EndTransaction(clientID, tr);
			/*
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
		}
		
		
		
	}
	
	// getters and setters for clientID, pManager and conn
	public int getClientID() {
		return clientID;
	}
	
	private void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public PersistenceManager getpManager() {
		return pManager;
	}

	private void setpManager(PersistenceManager pManager) {
		this.pManager = pManager;
	}

	public Connection getConn() {
		return conn;
	}

	private void setConn(Connection conn) {
		this.conn = conn;
	}

}