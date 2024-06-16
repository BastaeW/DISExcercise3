package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ModelBuilder 
{
	private static Connection connection;
	
	//Built the Crosstable
	public void builtModel()
	{
		String SQL = null; //TODO Einfügen!
		
        Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			ResultSetMetaData metaData = rs.getMetaData();
   	        int columnCount = metaData.getColumnCount();
   	     
   	        while (rs.next()) {
   	            for (int i = 1; i <= columnCount; i++) {
   	                if (i > 1) {
   	                    System.out.print("\t");
   	                }
   	                System.out.print(rs.getString(i));
   	            }
   	            System.out.println();

	        stmt.close();
			
		} 
   	    }
   	    catch (SQLException e) {
			System.out.println("Queryerzeugung ist fehlgeschlagen!");
		}
        
	}
	
	public void setConnection(Connection c)
	{
		connection = c;
	}
}
