package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelAnalyzer2 {

    private Connection connection;

    // Konstruktor
    public ModelAnalyzer2(Connection c) {
        this.connection = c;
    }

    // Analyse basierend auf den Dimensionen
    
    public void analyze(String geo, String time, String product) {
        try {
            analysis(geo, time, product);
        } catch (SQLException e) {
            System.out.println("Error occured during analysis");
            e.printStackTrace(); // Stacktrace für detaillierte Fehlermeldung
        }
    }

    /**
     * Datenanalyse basierend auf den angegebenen Dimensionen
     *
     * @param geo
     *        Erlaubte Werte: shop, city, region, country
     * @param time
     *        Erlaubte Werte: date, day, month, quarter, year
     * @param product
     *        Erlaubte Werte: article, productGroup, productFamily, productCategory
     * @throws SQLException
     */
    public void analysis(String geo, String time, String product) throws SQLException {
        String sql = generateSQLQuery(geo, time, product); // Dynamische SQL-Abfrage generieren
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // Abfrage ausführen + ResultSet erhalten
            printResults(rs);
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
    private String generateSQLQuery(String geo, String time, String product) {
        return "SELECT " + geo + ", " + time + ", " + product + ", SUM(\"Revenue\") AS total_revenue " +
               "FROM vsisp68.\"salesOrders\" " +
               "GROUP BY GROUPING SETS ((" + geo + ", " + time + ", " + product + "), " +
               "(" + geo + ", " + time + "), " +
               "(" + geo + ", " + product + "), " +
               "(" + time + ", " + product + "), " +
               "(" + geo + "), " +
               "(" + time + "), " +
               "(" + product + "), " +
               "()) " +
               "ORDER BY " + geo + ", " + time + ", " + product;
    }

    /**
     * Formatiert und druckt die Abfrageergebnisse in der Konsole
     *
     * @param rs
     *        ResultSet mit den Abfrageergebnissen
     * @throws SQLException
     */
    private void printResults(ResultSet rs) throws SQLException {
        while (rs.next()) { // Iteriere über die Ergebnisse
            String geoValue = rs.getString(1); // Geo-Dimension
            String timeValue = rs.getString(2); // Zeit-Dimension
            String productValue = rs.getString(3); // Produkt-Dimension
            double totalRevenue = rs.getDouble(4); // Aggregierter Umsatz

            // Formatiert die Ausgabe
            System.out.printf("%s | %s | %s | %.2f%n", geoValue, timeValue, productValue, totalRevenue);
        }
    }
}

