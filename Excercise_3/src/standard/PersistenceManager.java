package standard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public class PersistenceManager 
{
	//Singelton Instanz: Hält die Instanz des Objekts (verhindert die Erstellung weiterer Instanzen)
	private static PersistenceManager instance = null;
	//Puffer für zwischengespeicherte Daten
	private Hashtable<Integer, String> buffer;
	//Zähler für Transaktionen
	private int transactionCounter;
	//Liste für das Logging von Transactions
	//private List<String> log;

	
	private PersistenceManager() { 
	//privater Konstruktor, um die initalisierung außerhalb der Klasse zu vermeiden

		buffer = new Hashtable<>();
		transactionCounter = 0;
		//log = new ArrayList<>();
		try {
			FileUtils.cleanDirectory(new File("src/files/"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
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

	public void addToBuffer(int pageId, String data, Connection c) {
		String pageData = transactionCounter + ", "  + pageId + "; " + data;
		buffer.put(pageId, pageData);
		transactionCounter = transactionCounter + 1;
		
		if(buffer.size() >= 5)
		{
			persistBuffer();
			
			try {
				c.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			buffer = new Hashtable<>();
		}
		
	}
	
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
					
					writer.write(data); // Schreiben der Daten in eine Datei
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

	
	

/*

	//Methode zum Speichern des Logs
	public void saveLog() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/files/Test.txt"));
			for (String entry : log) {
				writer.write(entry); //Schreiben des Log-Eintrags in die Datei
				writer.newLine(); //Neue Zeile für den nächsten Eintrag
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
