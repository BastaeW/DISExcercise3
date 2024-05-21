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
        	PersistenceManager pManager = PersistenceManager.getInstance();
        	RecoveryManager rManager = new RecoveryManager();
        	
        	//rManager.recBuffer();
        	
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