package standard;

import java.io.*;
import java.util.stream.Collectors;

public class PersistenceManager 
{
	//Hält die Instanz des Objekts (verhindert die Erstellung weiterer Instanzen)
	private static PersistenceManager instance = null;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	
	private PersistenceManager()
	{
		try {
			setup();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Rückgabemethode für die Instanz
	public static synchronized PersistenceManager getInstance()
	{
		if(instance == null)
		{
			instance = new PersistenceManager();
		}
		//Gibt null oder den PersistenceManager (Instanz) zurück
		return instance;
		
	}
	
	private void setup() throws IOException
	{
		//Ausgeklammert für Implementation des Clients (muss danach noch auf den "richtigen" Pfad zeigen
		/*fileReader = new FileReader("C:\\Users\\sebas\\git\\repository\\Excercise_3\\src\\standard\\filetest.txt");
		bufferedReader = new BufferedReader(fileReader);
		
		String line = bufferedReader.readLine();

		while (line != null) {
			System.out.println(line);
			// read next line
			line = bufferedReader.readLine();
		}*/
	}
}
