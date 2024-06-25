package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ModelBuilder {
	private static Connection connection;

	// Built the Crosstable
	public void builtModel() {
		String SQL = "cREATE MATERiALiZEd ViEW vsisp68.\"salesorderViewM\" As \r\n" + "sELEcT \r\n" + "t1.\"son\",\r\n"
				+ "t1.\"Date\",\r\n" + "t1.\"Shop\",\r\n" + "t1.\"Article\",\r\n" + "t1.\"Sold\",\r\n"
				+ "t1.\"Revenue\",\r\n" + "t2.\"shopid\",\r\n" + "t2.\"cityid\",\r\n" + "t3.\"regionid\",\r\n"
				+ "t3.\"name\" As \"cityname\",\r\n" + "t4.\"countryid\",\r\n" + "t4.\"name\" As \"Regionname\",\r\n"
				+ "t5.\"name\" As \"countryname\",\r\n" + "t6.\"articleid\",\r\n" + "t6.\"productgroupid\",\r\n"
				+ "t6.\"price\",\r\n" + "t7.\"productfamilyid\",\r\n" + "t7.\"name\" As \"productgroupname\",\r\n"
				+ "t8.\"productcategoryid\",\r\n" + "t8.\"name\" As \"productfamilyname\",\r\n"
				+ "t9.\"name\" As \"productcategoyname\"\r\n" + "FRoM vsisp68.\"salesOrders\" As t1\r\n"
				+ "LEFT oUTER Join vsisp68.\"shop\" As t2 on t1.\"Shop\" = t2.\"name\"\r\n"
				+ "LEFT oUTER Join vsisp68.\"city\" As t3 on t3.\"cityid\" = t2.\"cityid\"\r\n"
				+ "LEFT oUTER Join vsisp68.\"region\" As t4 on t4.\"regionid\" = t3.\"regionid\"\r\n"
				+ "LEFT oUTER Join vsisp68.\"country\" As t5 on t5.\"countryid\" = t4.\"countryid\"\r\n"
				+ "LEFT oUTER Join vsisp68.\"article\" As t6 on t6.\"name\" = t1.\"Article\"\r\n"
				+ "LEFT oUTER Join vsisp68.\"productgroup\" As t7 on t7.\"productgroupid\" = t6.\"productgroupid\"\r\n"
				+ "LEFT oUTER Join vsisp68.\"productfamily\" As t8 on t8.\"productfamilyid\" = t7.\"productfamilyid\"\r\n"
				+ "LEFT oUTER Join vsisp68.\"productcategory\" As t9 on t9.\"productcategoryid\" = t8.\"productcategoryid\";";

		String del = "drop materialized view if exists vsisp68.\"salesorderViewM\";";

		String query = "Select * FROM vsisp68.\"salesorderViewM\";";
		Statement stmt;
		try {

			stmt = connection.createStatement();
			stmt.addBatch(del);
			stmt.addBatch(SQL);
			stmt.executeBatch();

			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					if (i > 1) {
						System.out.print("\t");
					}
					
					System.out.print(rs.getString(i));
					if(i==4 || i == 3 || i == 6)
					{
						for (int j = 0; j < 40-rs.getString(i).length(); j++) {
							System.out.print(" ");
							
						}
					}
				}
				System.out.println();
			}
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Queryerzeugung ist fehlgeschlagen!");
		}

	}

	public void setConnection(Connection c) {
		connection = c;
	}
}
