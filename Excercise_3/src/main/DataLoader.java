package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataLoader 
{
	private Connection connection;
	private ArrayList<String> goodLines;
	private ArrayList<String> corruptLines;
	private File fileInput;
	private File fileOutput;
	private int salesOrderNumber;
	
	//TODO Import CSV, check for corrupt lines
	public void loadData()
	{
		goodLines = new ArrayList<String>();
		corruptLines = new ArrayList<String>();
		
		fileInput = new File("src/data/sales.csv");
		
		String line;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(fileInput));
			
			 while ((line = br.readLine()) != null) 
			 {
				 if(checkDataTypes(line) == true)
				 {
					 goodLines.add(line);
				 }
				 else
				 {
					 corruptLines.add(line);
				 }
			 }
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("Die Datei konnte nicht eingelesen werden!");
		}
		
		try
		{
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO vsisp68.\"salesOrders\"(SON, Date, Shop, Article, Sold, Revenue) VALUES (?, ?, ?, ?, ?, ?)");
			
			for(String goodLine: goodLines)
			{
				String[] splitLine = goodLine.split(";");
				String[] splitDate = splitLine[0].split(".");
				LocalDate localDate = LocalDate.of(Integer.parseInt(splitDate[0]),Integer.parseInt(splitDate[1]),Integer.parseInt(splitDate[2]));
				
					
				stmt.setInt(1, salesOrderNumber); 
				stmt.setDate(2, Date.valueOf(localDate));
				stmt.setString(3, splitLine[1]);
				stmt.setString(4, splitLine[2]);
				stmt.setInt(5, Integer.parseInt(splitLine[3]));
				stmt.setDouble(6, Double.parseDouble(splitLine[4]));
					
			    stmt.addBatch();
					
				salesOrderNumber++;
				
			}
			
			stmt.executeBatch();
			stmt.close();
		}
		catch(Exception e)
		{
			System.out.println("Zeile konnte nicht eingelesen werden");
		}
		writeCSV();
	}
	

	private boolean checkDataTypes(String line) 
	{
		String[] splitLine = line.split(";");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		if(splitLine[1].length() <= 5000 && splitLine[2].length() <= 5000)
		{
			try
			{
				LocalDate.parse(splitLine[0], formatter);
				Integer.parseInt(splitLine[3]);
				Double.parseDouble(splitLine[4]);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}


	//TODO write corrupt lines to new CSV
	private void writeCSV()
	{
		fileOutput = new File("src/data/corruptSales.csv");
		
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOutput));
			for(String line: corruptLines)
			{
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			System.out.println("Fehlerhafte Datenzeilen wurden nicht importiert und in Ausgabedatei gesichert");
		}
		catch(Exception e)
		{
			System.out.println("Es wurden alle Datenzeilen importiert oder es konnte nicht in die Datei geschrieben werden");
		}
	}
	
	public void setSalesOrderNumber() 
	{
		Integer maxSON = 0;
		try
		{
			String SQL = "SELECT MAX(\"SON\") FROM vsisp68.\"salesOrders\"";
	        Statement stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery(SQL);
	        if (rs.next()) 
	        {
                maxSON = rs.getInt(1);
            }
	        stmt.close();
		}
		catch(Exception e)
		{
			maxSON = 0;	
		}
		salesOrderNumber = maxSON;
	}


	public void setConnection(Connection c) 
	{	
		connection = c;
	}
}
