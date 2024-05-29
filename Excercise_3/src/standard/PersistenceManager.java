package standard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;

public class PersistenceManager 
{
	//Singelton Instanz: Hält die Instanz des Objekts (verhindert die Erstellung weiterer Instanzen)
	private static PersistenceManager instance = null;
	//Puffer für zwischengespeicherte Daten
	private Hashtable<Integer, String> buffer;
	private AtomicInteger changeCounter;
	private AtomicInteger transactionCounter;
	private File file;
	private File file2;
	private List<String> transactions;
	
	private PersistenceManager() { 
	//privater Konstruktor, um die initalisierung außerhalb der Klasse zu vermeiden

		buffer = new Hashtable<>();
		changeCounter = new AtomicInteger(0);
		transactionCounter = new AtomicInteger(0);
		
		try {
			FileUtils.cleanDirectory(new File("src/files/"));
			FileUtils.cleanDirectory(new File("src/logs/"));
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
		file = new File("src/logs/ClientLog.txt");
		file2 = new File("src/logs/CommitedLog.txt");
	}
	
	//Rückgabemethode für die Instanz
	public static synchronized PersistenceManager getInstance() //Methode stellt sicher, dass nur eine Instanz des PM existiert und gibt die zurück, falls schon existiert
	{
		if(instance == null)
		{
			instance = new PersistenceManager();
		}
		//Gibt null oder den PersistenceManager (Instanz) zurück
		return instance;
	}
	
	// Add data to the buffer and persist if buffer is full
	public void addToBuffer(int pageId, String data, Connection c, int tr) {
		
		String pageData = changeCounter.get() + ", "  + pageId + "; " + data + "§ " + tr;
		buffer.put(changeCounter.get(), pageData);

		saveLine(pageData);
		changeCounter.getAndIncrement();
		
		
		if(buffer.size() >= 5)
		{
			//Logik --> alle commiteten Transaktionen (Operationen) entfernen (festschreiben)
			//buffer = new Hashtable<>();
		}
	}
	
	private void saveLine(String data)
	{
		BufferedWriter writer;
		System.out.println(data);
		try {
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(data); 
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int BeginnTransaction(int pageId) {
		AtomicInteger tr = transactionCounter;
		transactionCounter.getAndIncrement();
		String pageData = changeCounter.get() + ", "  + pageId + "; " + "<BOT>" + "§ " + tr;
		buffer.put(changeCounter.get(), pageData);
		saveLine(pageData);
        
		changeCounter.getAndIncrement();
		if(buffer.size() >= 5)
		{
			buffer = new Hashtable<>();
		}
		return tr.get();
	}
	
	// End a transaction and add it to the buffer
	public void EndTransaction(int pageId, int tr) {
		String pageData = changeCounter.get() + ", "  + pageId + "; " + "<EOT>" + "§ " + tr;
		buffer.put(changeCounter.get(), pageData);
		saveLine(pageData);
		
		changeCounter.getAndIncrement();
		
		BufferedWriter writer;
		System.out.println(pageData);
		try {
			writer = new BufferedWriter(new FileWriter(file2, true));
			writer.write(pageData); 
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int Filenumber = Character.getNumericValue(pageData.charAt(pageData.indexOf(";") - 1));
		File file3 = new File("src/files/Client" + Filenumber + ".txt");
		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(file3, true));
			writer2.write(pageData); 
			writer2.newLine();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
            while ((line = reader.readLine()) != null)
            {
            	if(line.substring(line.indexOf("§") + 2, line.length()).equals(String.valueOf(tr)) && !line.contains("<"))
            	{
            		writer2.write(line); 
        			writer2.newLine();
            	} 
            }
            
            reader.close();
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(buffer.size() >= 5)
		{
			buffer = new Hashtable<>();
		}
	}
	

	/*
	private void persistBuffer()
	{
		try {
			
			
			for (Map.Entry<Integer, String> entry : buffer.entrySet()) {
				  try
				  {
					String data = entry.getValue();
					int Filenumber = Character.getNumericValue(data.charAt(data.indexOf(";") - 1));
					
					File file = new File("src/files/Client" + Filenumber + ".txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					
					System.out.println(data);
					
					writer.write(data); 
					writer.newLine();
					writer.close();
				  }
				  catch(Exception e) {
					e.printStackTrace();
				   }
				}
			
		}
		catch(Exception e)
		{
			System.out.print(e);
		}
	}
	*/
}