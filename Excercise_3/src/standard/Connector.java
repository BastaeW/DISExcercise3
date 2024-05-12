package standard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connector {

	private static String host = "jdbc:postgresql://vsisdb.informatik.uni-hamburg.de:5432/dis-2024";
	private static String username = "vsisp68";
	private static String password = "PcPRK4Oz";
	public Connection con;
	
	public void connect() {
		
		try {
			con = DriverManager.getConnection(host, username, password);
			System.out.println("Successfull Connection");
		} 
		catch (SQLException e) {
			System.out.println("Unsuccessfull Connection, program terminates!");
		}
		
	}
	
	public Connection getConnection()
	{
		return con;
	}

}
