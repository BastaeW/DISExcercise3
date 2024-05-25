package standard;


import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    public static Connector ctr;

    public static Connection conn;

    public static void main(String[] args) {
        ctr = new Connector();
        connect(ctr);
        conn = ctr.getConnection();

        if(conn != null)
        {
        	//PersistenceManager pManager = PersistenceManager.getInstance();
        	RecoveryManager rManager = new RecoveryManager(conn);
        	
        	//Wenn PersistenceManager genutzt werden soll, dann bitte Zeile 20 einkommentieren, Zeile 44 auskommentieren und den folgenden Block einkommentieren (24-43)
        	/*
        	int clientNumber = 5;
        	ExecutorService executor = Executors.newFixedThreadPool(clientNumber);
        	
        	for (int i = 0; i< clientNumber; i++)
        	{
        		final int threadID = i;
        		executor.execute(new Runnable() {
					@Override
					public void run() {
						
						new Client(threadID, pManager, conn);
					
					}
        			
        		});
        	}
        	executor.shutdown();
        	
        */
        	rManager.recover();
        }     
        else
        {
        	System.exit(0);
        }
        
}

    public static void connect(Connector ctr) {
        ctr.connect();
    }

    public Connector getConnector() {
        return ctr;
    }
}