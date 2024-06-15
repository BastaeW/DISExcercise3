package main;

import java.sql.*;

public class Connector {

	private static String host = "jdbc:postgresql://vsisdb.informatik.uni-hamburg.de:5432/dis-2024";
	private static String username = "vsisp68";
	private static String password = "PcPRK4Oz";
	private Connection connection;
	
	public Connection connect()
	{
		try {
			connection = DriverManager.getConnection(host, username, password);
		} 
		catch (SQLException e) {
			
		}
		return connection;
	}
	
	public Connection getConnection()
	{
		return connection;
	}
}
