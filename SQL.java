import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void ps_add_drone(String sql, int serialNumber, String name,
            String model, boolean status, String location, String manufacturer,
            int year, double wc, int distance, int maxSpeed, String we) {

        try {
            ps = TextDB.conn.prepareStatement(sql);
            ps.setInt(1, serialNumber);
            ps.setString(2, name);
            ps.setString(3, model);
            ps.setBoolean(4, status);
            ps.setString(5, location);
            ps.setString(6, manufacturer);
            ps.setInt(7, year);
            ps.setDouble(8, wc);
            ps.setInt(9, distance);
            ps.setInt(10, maxSpeed);
            ps.setDate(11, Date.valueOf(we));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void ps_delete_drone(String sql, int s) {
        try {
            ps = TextDB.conn.prepareStatement(sql);
            ps.setInt(1, s);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Drone deleted.");

    }

    public static void ps_update_drone(String sql, int s, int n) {

        try {
            ps = TextDB.conn.prepareStatement(sql);
            ps.setInt(1, s);
            ps.setInt(2, n);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Drone Updated");
        }

    }

    public static void ps_search_drone(String sql, int n) {

        try {
            ps = TextDB.conn.prepareStatement(sql);
            ps.setInt(1, n);
            sqlQuery(TextDB.conn, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Here:");
    }
}
