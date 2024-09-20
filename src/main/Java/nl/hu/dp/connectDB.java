package nl.hu.bp;

import java.sql.*;

// JDBC interface for database operations
interface Database {
    void connect();
    void selectAllReizigers();
    void insertGebruiker(String klantNaam, String geboorteDatum);
}

// Implementation of the JDBC interface
public class connectDB implements Database {

    private Connection connection;

    public static void main(String[] args) {
        Database dbInstance = new connectDB();
        dbInstance.connect();

        dbInstance.selectAllReizigers();
        // dbInstance.insertGebruiker("H. van der Ridder", "1962-02-01");
    }

    @Override
    public void connect() {
        String url = "jdbc:postgresql://localhost:5432/OV_Chipkaart";
        String user = "postgres";
        String password = "fj6YDB5D";

        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to PostgreSQL server established.");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        }
    }

    @Override
    public void insertGebruiker(String klantNaam, String geboorteDatum) {
        String sql = "INSERT INTO public.reizigers(naam, geboortedatum) VALUES (?, ?);";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, klantNaam);

            java.sql.Date sqlDate = java.sql.Date.valueOf(geboorteDatum);
            stmt.setDate(2, sqlDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Gebruiker inserted successfully.");
            } else {
                System.out.println("Failed to insert Gebruiker with klantNaam: " + klantNaam);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    @Override
    public void selectAllReizigers() {
        String sql = "SELECT naam, geboortedatum FROM public.reizigers;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();

            System.out.println("Alle reizigers:");

            // Loop through the ResultSet and print each traveler
            while (resultSet.next()) {
                String naam = resultSet.getString("naam");
                java.sql.Date geboortedatum = resultSet.getDate("geboortedatum");

                // Print the result
                System.out.println("Naam: " + naam + ", Geboortedatum: " + geboortedatum);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
}
