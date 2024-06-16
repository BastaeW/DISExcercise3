package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelAnalyzer {

	//TODO implement the GroupingSets for the Analysis and implement the user inputs as well as the output
	/*ToDos ModelAnalyzer
	 * 1. Dataqueries based on the following three dimensions (Time, Store, Product)
	 * 2. Navigation across the dimensional hierachie (Drill Down and Roll Up)
	 * 3. Printing of the Results
	 */
	Private Connection connection; 

	public void analyze() {
		
	}
	
	//Name der materialisierten View für die Auswertung: "SalesOrderViewM"
	
	ModelAnalyzer(Connection connection){
		this.Connection = connection;
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
		try (PreparedStatement stmt = connection.preparedStatement(sql); //Erstellung eines PreparedStatement Objekts, dass die SQL Abfarge enthält
				ResultSet rs = stmtexecuteQuery()) { //Führt die Abfrage aus und gibt ein ResultSet zurück, dass die Ergebnisse enthält
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
	private String generateSQLQuery(String gwo, String time, String product) { //Parameter definieren, nach welchen Dimensionen die Daten aggregiert werden sollen
		return "SELECT " + geo + ", " + time ", " + product ", SUM(turnover) AS total_turnover " + //Select Klausel wählt Dimensionen und berechnet die Summe der Umsätze (total_turnover)
				"FROM sales_data " +
				"GROUP BY " + geo + ", " + time + ", " + product + " " + // Gruppieren der Daten nach den angegebenen Dimensionen, sodass SUM(zutnover) für jede Gruppe berechnet wird
				"WITH ROLLUP"; //Ermöglicht zusammengefasste Daten auf verschd. Aggregationsstufen zu berechnen. Zwischen und Gesamtsummen können berechnet werden
	}
	
	/**
     * Formatiert und druckt die Abfrageergebnisse in der Konsole.
     *
     * @param rs
     *        ResultSet mit den Abfrageergebnissen
     * @throws SQLException
     */
	
	private void printResults(ResultSet rs) throws SQLException {
		while (rs.next()) { //Läuft solange es noch Zeilen im ResultSet "rs" gibt --> wenn keine Zeilen mehr dann false als return
			//Abrufen der Werte aus dem resultSet
			 String geoValue = rs.getString(1); // ruft den Wert der 1 Spalte ab (geo)
	         String timeValue = rs.getString(2); // ruft den Wert der 2 Spalte ab (time)
	         String productValue = rs.getString(3); // ruft den Wert der 3 Spalte ab (product)
	         double totalTurnover = rs.getDouble(4); // ruft den Wert der 4 Spalte ab (total_turnover)
	         
	         System.out.printf("%s | %s | %s | %.2f%n", geoValue, timeValue, productValue, totalTurnover); // Ausgabe der Werte in formatierten String (%s = String und %.2f = Fließkommazahl)
		}
	}
	
}
