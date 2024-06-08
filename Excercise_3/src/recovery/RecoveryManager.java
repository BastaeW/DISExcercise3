package recovery;

import client.DataObject;
import persistence.PersistenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecoveryManager {

    static final private RecoveryManager _manager;
    private ArrayList<Integer> pageLSN;
    // TODO Add class variables if necessary
    PersistenceManager pm;

    static {
        try {
            _manager = new RecoveryManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private RecoveryManager() {
        // TODO Initialize class variables if necessary
    	pageLSN = new ArrayList<Integer>();
        pm = PersistenceManager.getInstance();
    }

    static public RecoveryManager getInstance() {
        return _manager;
    }

    public void startRecovery() {
        ArrayList<String> win = findWinners();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/files/Datalog.txt"));
            String line = reader.readLine();

            while (line != null && !line.equals("")) {
                String[] splitted = line.split(",");
                if (win.contains(splitted[1].strip()) && !splitted[2].strip().equals("BOT") && !splitted[2].strip().equals("EOT")) { //If it is a winning transaction
                    tryRecovery(splitted[0].strip(), splitted[1].strip(), splitted[2].strip(), splitted[3].strip());
                }

                //System.out.println(splitted[2]);
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void tryRecovery(String lsn, String transID, String page, String info) {
        File f = new File("src/pages/" + page + ".txt");
        if(f.exists() && !f.isDirectory()) {


        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/pages/" + page + ".txt"));
            String line = reader.readLine();

            while (line != null && !line.equals("")) {
                String[] splitted = line.split(",");
                if(Integer.valueOf(splitted[0].strip()) < Integer.valueOf(lsn))
                {
                    System.out.println("Recovering lsn: " + lsn + " and inserting data on page: "+ page);
                    DataObject newData = new DataObject(Integer.valueOf(transID), info, Integer.valueOf(lsn));
                    pm.writeToPersistentStorage(Integer.valueOf(page), newData);
                }
                break;
            }


            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }}
        else {
            System.out.println("Generating page:" + page + " for recovery of lsn: " + lsn);
            DataObject newData = new DataObject(Integer.valueOf(transID), info, Integer.valueOf(lsn));
            pm.writeToPersistentStorage(Integer.valueOf(page), newData);
        }
    }

    private ArrayList<String> findWinners() {
        BufferedReader reader;
        ArrayList<String> winners = new ArrayList<String>();

        try {
            reader = new BufferedReader(new FileReader("src/files/Datalog.txt"));
            String line = reader.readLine();


            while (line != null && !line.equals("")) {
                String[] splitted = line.split(",");
                //System.out.println(splitted[0].strip());
                if (splitted[2].strip().equals("EOT")) {
                    winners.add(splitted[1]);
                }

                //System.out.println(splitted[2]);
                // read next line
                line = reader.readLine();
            }
            /*
            for (String winner : winners) {
                System.out.println(winner);
            }*/

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return winners;
    }
}

