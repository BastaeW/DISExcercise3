package standard;

import java.io.*;

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
		fileReader = new FileReader("Filetest.txt");
		bufferedReader = new BufferedReader(fileReader);
		
		while(bufferedReader.readLine() != null)
		{
			System.out.println(bufferedReader.readLine());
		}
	}
}
