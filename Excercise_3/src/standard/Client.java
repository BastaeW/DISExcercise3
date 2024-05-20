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
		for(int i = 0; i<5; i++)
		{
			int randTable = rand.nextInt(2) + 1;
			int randRow = rand.nextInt(10) + 1;
			int randValue = rand.nextInt(1000);
			int randSleep = (rand.nextInt(5)*1000) + 1000;
			String SQL = "UPDATE vsisp68.\"ex4table" + randTable + "\" SET" + " \"counter\" = '" + randValue + "' WHERE \"row\" = " + randRow;
			
			try {
				Statement stmt = conn.createStatement();
	            stmt.execute(SQL);
			}
			catch(Exception e)
			{
				System.out.println("war nicht ausführbar!");
			}
			
			System.out.println(SQL);
			
			try {
				Thread.sleep(randSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
