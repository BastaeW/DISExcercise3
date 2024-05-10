package standard;

import java.sql.Connection;

public class Controller {
    public static Connector ctr;

    public static Connection conn;

    public static void main(String[] args) {
        ctr = new Connector();
        connect(ctr);
        conn = ctr.getConnection();

        //Weiterer Programmablauf, bei erfolgreicher Connection
        
    }

    public static void connect(Connector ctr) {
        ctr.connect();
    }

    public Connector getConnector() {
        return ctr;
    }
}