package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
	
	private static Connector connector;
	private static Connection connection;
	private static DataLoader dataLoader;
	private static ModelBuilder modelBuilder;
	private static ModelAnalyzer modelAnalyzer;	
	
	
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
			dataLoader.setConnection(connection);
			dataLoader.loadData();
			
			modelBuilder = new ModelBuilder();
			modelBuilder.setConnection(connection);
			modelBuilder.builtModel();
			
			modelAnalyzer = new ModelAnalyzer(connection);

			boolean run = true;
			while(run == true)
			{
				run = userInput();
			}
			
			System.exit(0);
			
		}
    }
	
	public static boolean userInput()
	{
		Scanner ein = new Scanner(System.in);
		System.out.println();
		System.out.println("Geben Sie die Geo-Ebene ein (Shop, cityname, Regionname, countryname): ");
		String geo = ein.nextLine();
		System.out.println();
		
		System.out.println("Geben Sie die Zeit-Ebene ein (Date, Day, Week, Month, Quarter, Year): ");
		String time = ein.nextLine();
		System.out.println();
		
		System.out.println("Geben Sie die Produkt-Ebene ein (Leer, Article, productgroupname, productfamilyname, productcategoryname): ");
		String prod = ein.nextLine();
		
		
		modelAnalyzer.analyze(geo, time, prod);
		
		System.out.println("Weiter Abfrage? (y/n):");
		String run = ein.nextLine();

		if(run.equals("y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
