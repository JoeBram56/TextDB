import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 *
 * All database connectivity should be handled from within here.
 *
 */
public class SQL {

    private static PreparedStatement ps;

    /**
     * Queries the database and prints the results.
     *
     * @param conn
     *            a connection object
     * @param sql
     *            a SQL statement that returns rows: this query is written with
     *            the Statement class, typically used for static SQL SELECT
     *            statements.
     */
    public static void sqlQuery(Connection conn, String sql) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rsmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount) {
                    System.out.print(",  ");
                }
            }
            System.out.print("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                    if (i < columnCount) {
                        System.out.print(",  ");
                    }
                }
                System.out.print("\n");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Queries the database and prints the results.
     *
     * @param conn
     *            a connection object
     * @param sql
     *            a SQL statement that returns rows: this query is written with
     *            the PrepareStatement class, typically used for dynamic SQL
     *            SELECT statements.
     */
    public static void sqlQuery(Connection conn, PreparedStatement sql) {
        try {
            ResultSet rs = sql.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rsmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount) {
                    System.out.print(",  ");
                }
            }
            System.out.print("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                    if (i < columnCount) {
                        System.out.print(",  ");
                    }
                }
                System.out.print("\n");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void executeRentEquipment(String serialNumber,
            String updateStatusSQL, String insertRentalSQL) {

        // Generate random values
        int userId = new Random().nextInt(1000) + 1000; // Random user ID between 1000 and 1999
        double rentalFee = 20.0 + new Random().nextDouble() * 30.0; // Random rental fee between 20.0 and 50.0

        // Dates
        String checkOutDate = LocalDate.now()
                .format(DateTimeFormatter.ISO_DATE);
        String dueDate = LocalDate.now().plusDays(7)
                .format(DateTimeFormatter.ISO_DATE);

        try (Connection conn = TextDB.conn;
                PreparedStatement ps1 = conn.prepareStatement(updateStatusSQL);
                PreparedStatement ps2 = conn
                        .prepareStatement(insertRentalSQL)) {

            conn.setAutoCommit(false); // Begin transaction

            // Update equipment status
            ps1.setString(1, serialNumber);
            int equipmentUpdated = ps1.executeUpdate();

            if (equipmentUpdated > 0) {
                // Insert rental record
                ps2.setInt(1, userId);
                ps2.setString(2, serialNumber);
                ps2.setString(3, dueDate);
                ps2.setString(4, checkOutDate);
                ps2.setDouble(5, rentalFee);
                ps2.executeUpdate();

                conn.commit(); // Commit transaction
                System.out.println("Equipment rented successfully.");
                System.out.println("User ID: " + userId);
                System.out.println(
                        "Rental Fee: $" + String.format("%.2f", rentalFee));
            } else {
                conn.rollback(); // Roll back transaction if equipment not found
                System.out.println(
                        "Error: Equipment not found or already rented.");
            }

        } catch (SQLException e) {
            System.out.println("Error executing rental: " + e.getMessage());
        }

    }

}
