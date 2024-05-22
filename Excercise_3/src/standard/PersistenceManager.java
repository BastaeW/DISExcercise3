package standard;

import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class PersistenceManager 
{
	//Singelton Instanz: Hält die Instanz des Objekts (verhindert die Erstellung weiterer Instanzen)
	private static PersistenceManager instance = null;
	//Puffer für zwischengespeicherte Daten
	private Hashtable<Integer, String> buffer;
	//Zähler für Transaktionen
	private int transactionCounter;
	//Liste für das Logging von Transactions
	private List<String> log;
	//private FileReader fileReader;
	//private BufferedReader bufferedReader;
	
	private PersistenceManager() { 
	//privater Konstruktor, um die initalisierung außerhalb der Klasse zu vermeiden
	/*Habe die Initialisierungslogik entfernt und rufe hier stattdessen die setup() Methode auf --> Konstruktor wird schlanker.
	Die Initialisierung des PersistenceManager-Objekts wird in die setup()-Methode ausgelagert, die im Falle eines Fehlers eine IOException auslöst
	{
		/*try {
			setup();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		buffer = new Hashtable<>();
		transactionCounter = 0;
		log = new ArrayList<>();
		
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
	
	//Methode zum Starten einer neuen Transaktion
	public synchronized int beginTransaction() {
		return ++transactionCounter; // erhöht TransactionCounter und gibt eine neue Transaction-ID zurück
		
	}
	
	//Methode zum Committen einer Transaction
	public synchronized void write(int taid, int pageid, String data) {
		String pageData = String.format("%d,%d%s",  transactionCounter, pageid, data); //Formatierung der Page-Daten
		buffer.put(pageid,  pageData)//Hinzufügen der Daten zum Puffer
		log.add(pageData); // Hinzufügen der Daten zum Log
		if (buffer.size() < 5) { //Überprüfe, ob der Puffer größer als 5 Einträge ist
			flushBuffer(); // wenn ja, dann Daten aus dem Buffer in persistenten Speicher schreiben
			
		}
	}
	
	//Methode zum Leeren des Buffers
	private vooid flushBuffer() {
		for (Map.Entry<Integer, String> entry : buffer.entrySet()) {
			int pageid = entry.getKey();
			String data = entry.getValue();
			writeToFile(pageid, data); // Schreiben der Daten in eine Datei
		} catch(I0Exception e) {
			e.printStackTrace();
		}
	}
	
	//Methode zum Speichern des Logs
	public void saveLog() {
		try (BufferedWriter write = new BufferedWriter(newFil eWriter("log.txt"))) {
			for (String entry : log) {
				writer.write(entry); //Schreiben des Log-Eintrags in die Datei
				writer.newLine(); //Neue Zeile für den nächsten Eintrag
			}
		} catch (I0Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private void setup() throws IOException
	{
		//Ausgeklammert für Implementation des Clients (muss danach noch auf den "richtigen" Pfad zeigen
		/*fileReader = new FileReader("C:\\Users\\sebas\\git\\repository\\Excercise_3\\src\\standard\\filetest.txt");
		bufferedReader = new BufferedReader(fileReader);
		
		String line = bufferedReader.readLine();

		while (line != null) {
			System.out.println(line);
			// read next line
			line = bufferedReader.readLine();
		}
	}*/
}
