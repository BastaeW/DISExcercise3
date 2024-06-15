package main;

import java.sql.*;
import java.util.ArrayList;

public class Controller {
	
	private static Connector connector;
	private static Connection connection;
	private static DataLoader dataLoader;
	private static ModelBuilder modelBuilder;
	private static ModelAnalyzer modelAnalyzer;
	
	private static ArrayList<String> goodLines;
	
	
	public static void main(String[] args) {
		connector = new Connector();
		connection = connector.connect();
		
		if(connection.equals(null))
		{
			System.out.println("Unsuccessfull Connection, program terminates!");
			System.exit(0);
		}
		else
		{
			System.out.println("Successfull Connection");
			dataLoader = new DataLoader();
			goodLines = dataLoader.loadData();
			
			modelBuilder = new ModelBuilder();
			modelBuilder.setConnection(connection);
			
			modelAnalyzer = new ModelAnalyzer();
			modelAnalyzer.analyze();
		}
    }

}
