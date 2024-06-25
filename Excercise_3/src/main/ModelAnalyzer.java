package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ModelAnalyzer {

	private Connection connection;
	//TODO implement the GroupingSets for the Analysis and implement the user inputs as well as the output
	/*ToDos ModelAnalyzer
	 * 1. Dataqueries based on the following three dimensions (Time, Store, Product)
	 * 2. Navigation across the dimensional hierachie (Drill Down and Roll Up)
	 * 3. Printing of the Results
	 */
	

	public void analyze(String geo, String time, String product) {
        try {
            analysis(geo, time, product);
        } catch (SQLException e) {
            System.out.println("Error occured during analysis");
            e.printStackTrace(); // Stacktrace für detaillierte Fehlermeldung
        }
    }
	
	//Name der materialisierten View für die Auswertung: "SalesOrderViewM"
	
	ModelAnalyzer(Connection c){
		this.connection = c;
	}
	/**
     * Führt die Datenanalyse basierend auf den angegebenen Dimensionen durch.
     *
     * @param geo
     *        Erlaubte Werte: shop, city, region, country
     * @param time
     *        Erlaubte Werte: date, day, month, quarter, year
     * @param product
     *        Erlaubte Werte: article, productGroup, productFamily, productCategory
     * @throws SQLException
     */
	public void analysis (String geo, String time, String product) throws SQLException {
		String sql = generateSQLQuery(geo, time, product); //Ruft Methode generateSQLQuery auf und übergibt Parameter. Methode erstellt eine SQl Abfrage basierend auf den übergebenen Dimensionen und gibt Sie als String zurück
		try (PreparedStatement stmt = connection.prepareStatement(sql); //Erstellung eines PreparedStatement Objekts, dass die SQL Abfarge enthält
				ResultSet rs = stmt.executeQuery()) { //Führt die Abfrage aus und gibt ein ResultSet zurück, dass die Ergebnisse enthält
			printResults(rs); //Ruft Methode printResults auf und übergibt das rs Objekt an sie --> Liest die Daten aus dem ResultSet und gibt auf Konsole aus
		}
				
	}
	/**
     * Generiert die SQL-Abfrage basierend auf den Dimensionen.
     *
     * @param geo
     * @param time
     * @param product
     * @return SQL-Abfrage als String
     */
	private String generateSQLQuery(String geo, String time, String product) { //Parameter definieren, nach welchen Dimensionen die Daten aggregiert werden sollen
		String geoharm = geo;
		String timeharm = time;
		String prodharm = product;
		
		if(geoharm == "")
		{
			geoharm = geo;
		}
		else
		{
			geoharm = "\"" + geo + "\",";
		}
		if(prodharm == "")
		{
			prodharm = product;
		}
		else
		{
			prodharm = "\"" + product + "\",";
		}
		
		if(timeharm == "")
		{
			timeharm = time;
		}
		else
		{
			if(timeharm.equals("Date")||timeharm.equals("date"))
			{
				timeharm = "\"" + time + "\",";
			}
			else
			{
				timeharm = "date_part('" + time + "', \"Date\"),";
			}
		}
		
		String selectfield = geoharm + timeharm + prodharm;
		String groupfield = geoharm + timeharm + prodharm;
		
		if(groupfield.endsWith(","))
		{
			groupfield = groupfield.substring(0, groupfield.length()-1);
		}
		
		String prepSQL =  "SELECT" + selectfield + " SUM(\"Sold\"), SUM(\"Revenue\")" + //Select Klausel wählt Dimensionen und berechnet die Summe der Umsätze (total_turnover)
				"FROM vsisp68.\"salesorderViewM\" " ;
		if(!groupfield.isEmpty())
		{
			prepSQL = prepSQL + "GROUP BY CUBE(" + groupfield + ");";//Ermöglicht zusammengefasste Daten auf verschd. Aggregationsstufen zu berechnen. Zwischen und Gesamtsummen können berechnet werden	
		}
		else
		{
			prepSQL = prepSQL + ";";
		}
		return prepSQL;
	}
	
	/**
     * Formatiert und druckt die Abfrageergebnisse in der Konsole.
     *
     * @param rs
     *        ResultSet mit den Abfrageergebnissen
     * @throws SQLException
     */
	
	private void printResults(ResultSet rs) throws SQLException {
		//while (rs.next()) { //Läuft solange es noch Zeilen im ResultSet "rs" gibt --> wenn keine Zeilen mehr dann false als return
			//Abrufen der Werte aus dem resultSet
			 /*String geoValue = rs.getString(1); // ruft den Wert der 1 Spalte ab (geo)
	         String timeValue = rs.getString(2); // ruft den Wert der 2 Spalte ab (time)
	         String productValue = rs.getString(3); // ruft den Wert der 3 Spalte ab (product)
	         double totalTurnover = rs.getDouble(4); // ruft den Wert der 4 Spalte ab (total_turnover)
	         double totalRevenue = rs.getDouble(5);
	         
	         System.out.printf("%s | %s | %s | %.2f| %.2f%n", geoValue, timeValue, productValue, totalTurnover, totalRevenue); // Ausgabe der Werte in formatierten String (%s = String und %.2f = Fließkommazahl)
		*/

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
		
		
		//}
	}
	
}
}
