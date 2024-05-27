package standard;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;


public class Client {
	
	private int clientID;
	private PersistenceManager pManager;
	private Connection conn;
	
	public Client(int clientIdentifier, PersistenceManager persistenceManager, Connection connection)
	{
		setClientID(clientIdentifier);
		setpManager(persistenceManager);
		setConn(connection);
		
		run();
	}
	
	public void run()
	{
		Random rand = new Random();
		try {
			Thread.sleep(rand.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<5; i++)
		{
			
			int randTable = rand.nextInt(2) + 1;
			int randRow = rand.nextInt(10) + 1;
			int randValue = rand.nextInt(1000);
			//int randSleep = rand.nextInt(500) + (1*clientID);
			int randSleep = 1 + (1000*clientID);
			String SQL = "UPDATE vsisp68.\"ex4table" + randTable + "\" SET" + " \"counter\" = '" + randValue + "' WHERE \"row\" = " + randRow;
			
			try {
				conn.setAutoCommit(false);
				pManager.addToBuffer(clientID, SQL, conn);
				Statement stmt = conn.createStatement();
	            stmt.execute(SQL);
	            Thread.sleep(randSleep);
			}
			catch(Exception e)
			{
				System.out.println("war nicht ausführbar!");
			}
			
		}
		
	}
	
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