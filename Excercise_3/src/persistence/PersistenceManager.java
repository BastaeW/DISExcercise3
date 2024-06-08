package persistence;

import client.DataObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class PersistenceManager {

    static final private PersistenceManager _manager;

     private static List<Integer> idStorage = new ArrayList<Integer>();
     private static Hashtable<Integer, DataObject > buffer = new Hashtable();
     private static int lastTransactionId = 0;
     private static List<Integer> commitedTransactions = new ArrayList<Integer>();
     private static int lastLSN = 0;
    // TODO Add class variables if necessary


	static {
        try {
            _manager = new PersistenceManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private PersistenceManager() {
    	
        // TODO Get the last used transaction id from the log (if present) at startup
        // TODO Initialize class variables if necessary
    	getVariablesFromLog();
    	idStorage.add(lastTransactionId);
    }

    private void getVariablesFromLog() {
    	Integer tID = 0;
    	Integer LSN = 0;

        try{
        	BufferedReader br = new BufferedReader(new FileReader("src/files/Datalog.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    try {
                        int number = Integer.parseInt(parts[0].trim());
                        if (number > LSN) {
                            LSN = number;
                        }
                        int number2 = Integer.parseInt(parts[1].trim());
                        if (number2 > tID) {
                            tID = number2;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Ungültige Nummer in Zeile: " + line);
                    }
                }
            }
        }
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    	setLastTransactionId(tID);
		setLastLSN(LSN);
	}

	static public PersistenceManager getInstance() {
        return _manager;
    }

    public synchronized int beginTransaction() {

        while (idStorage.contains(lastTransactionId))
        {
            lastTransactionId++;

        }
        idStorage.add(lastTransactionId);
        writeToLog(lastTransactionId, "BOT");
        // TODO return a valid transaction id to the client
        return lastTransactionId;
    }

    private void writeToLog(int transactionId, String data) {
        int lsn = generateLSN();
        try(FileWriter fileWriter = new FileWriter("src/files/Datalog.txt", true)){
            //FileWriter fileWriter = new FileWriter("log.txt");
            fileWriter.write(lsn + "," + transactionId + "," + data + "\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void writeToLog(int transactionId, int pageId,  String data, int lsn) {
        try(FileWriter fileWriter = new FileWriter("src/files/Datalog.txt", true)){

            fileWriter.write(lsn + "," + transactionId + "," + pageId + "," + data + "\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void commit(int taid) {
        writeToLog(taid, "EOT");
        commitedTransactions.add(taid);
        
        if(taid % 2 == 0)
        {
        	flushBuffer();
        }
        // TODO handle commits
    }

    public void write(int taid, int pageid, String data) {
        // TODO handle writes of Transaction taid on page pageid with data
        try{
            int lsn = generateLSN();
            DataObject dataObj = new DataObject(taid, data, lsn);
            writeToLog(taid,pageid,data, lsn);
            buffer.put(pageid, dataObj);
            final List<Integer> toRemoveLater = new ArrayList<Integer>();
            if(buffer.size()>5)
            {
                buffer.forEach((key, value) -> {
                    System.out.println("key: " + key + " value: " + value);
                    if(commitedTransactions.contains(value.getTransactionId()))
                    {
                        System.out.println("To persistence: " + key + ":" + value.toString());
                        writeToPersistentStorage(key, value);
                        toRemoveLater.add(key);
                        //buffer.remove(key);
                    }
                });
                for (int i : toRemoveLater) {
                    buffer.remove(i);
                }
                toRemoveLater.clear();
            }
            //FileWriter fw = new FileWriter(pageid+".txt");

        }catch(Exception e)
        {
            e.getStackTrace();
        }


    }
    public void flushBuffer()
    {
    	buffer.forEach((key, value) -> {
    		
    		File f = new File("src/pages/" + key + ".txt");
    		if(f.exists() == true) {
    		try {
                BufferedReader reader = new BufferedReader(new FileReader("src/pages/" + key + ".txt"));
                String line = reader.readLine();

                while (line != null && !line.equals("")) {
                    String[] splitted = line.split(",");
                    if(Integer.valueOf(splitted[0].strip()) < Integer.valueOf(key))
                    {
                    	 writeToPersistentStorage(key, value);
                    }
                    
                }
            }
    		catch(Exception e)
    		{
    			System.out.println(e);
    		}
    	}
    		else {writeToPersistentStorage(key, value);}
    		});
    }

    private int generateLSN() {
        lastLSN++;
        return lastLSN;
    }

    public void writeToPersistentStorage(Integer pageId, DataObject value) {
        try(FileWriter fw = new FileWriter("src/pages/" + pageId+".txt")){
            //FileWriter fw = new FileWriter(pageId+".txt");
            fw.write(value.getLSN() + "," + value.getData() + "; "+ "\n");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getLastTransactionId() {
		return lastTransactionId;
	}

	public static void setLastTransactionId(int lastTransactionId) {
		PersistenceManager.lastTransactionId = lastTransactionId;
	}

	public static int getLastLSN() {
		return lastLSN;
	}

	public static void setLastLSN(int lastLSN) {
		PersistenceManager.lastLSN = lastLSN;
	}
	
}
