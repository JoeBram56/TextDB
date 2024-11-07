import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;


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

      public static void executeRentEquipment(String serialNumber, int userId,
            String updateStatusSQL, String insertRentalSQL) {
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {
            // Begin transaction
            TextDB.conn.setAutoCommit(false);

            // Update equipment status
            ps1 = TextDB.conn.prepareStatement(updateStatusSQL);
            ps1.setString(1, serialNumber);
            int equipmentUpdated = ps1.executeUpdate();

            if (equipmentUpdated > 0) {
                // Generate a random rental fee within a range
                double rentalFee = 20.0 + new Random().nextDouble() * 30.0;

                // Dates
                String checkOutDate = LocalDate.now()
                        .format(DateTimeFormatter.ISO_DATE);
                String dueDate = LocalDate.now().plusDays(7)
                        .format(DateTimeFormatter.ISO_DATE);

                // Insert rental record
                ps2 = TextDB.conn.prepareStatement(insertRentalSQL);
                ps2.setInt(1, userId);
                ps2.setString(2, serialNumber);
                ps2.setString(3, dueDate);
                ps2.setString(4, checkOutDate);
                ps2.setDouble(5, rentalFee);
                ps2.executeUpdate();

                // Commit transaction
                TextDB.conn.commit();
                System.out.println("Equipment rented successfully.");
                System.out.println("User ID: " + userId);
                System.out.println(
                        "Rental Fee: $" + String.format("%.2f", rentalFee));
            } else {
                TextDB.conn.rollback();
                System.out.println(
                        "Error: Equipment not found or already rented.");
            }

        } catch (SQLException e) {
            System.out.println("Error executing rental: " + e.getMessage());
            try {
                TextDB.conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println(
                        "Error during rollback: " + rollbackEx.getMessage());
            }
        } finally {
            // Close resources to prevent locks
            try {
                if (ps1 != null) {
                    ps1.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
            try {
                if (ps2 != null) {
                    ps2.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
        }
    }

     public static void executeDeliverEquipment(int warehouseId,
            String serialNumber, String fetchWarehouseSQL,
            String updateStatusSQL) {
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            // Begin transaction
            TextDB.conn.setAutoCommit(false);

            // Fetch warehouse address and city
            ps1 = TextDB.conn.prepareStatement(fetchWarehouseSQL);
            ps1.setInt(1, warehouseId);
            rs = ps1.executeQuery();

            if (rs.next()) {
                String warehouseAddress = rs.getString("Address");
                String warehouseCity = rs.getString("City");

                // Update equipment status
                ps2 = TextDB.conn.prepareStatement(updateStatusSQL);
                ps2.setInt(1, warehouseId);
                ps2.setString(2, serialNumber);
                int equipmentUpdated = ps2.executeUpdate();

                if (equipmentUpdated > 0) {
                    TextDB.conn.commit();
                    System.out.println("Equipment delivered successfully.");
                    System.out.println(
                            "Delivered to Warehouse ID: " + warehouseId);
                    System.out.println("Warehouse Location: " + warehouseAddress
                            + ", " + warehouseCity);
                } else {
                    TextDB.conn.rollback();
                    System.out.println(
                            "Error: Equipment not found or already delivered.");
                }
            } else {
                System.out.println("Error: Warehouse not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error executing delivery: " + e.getMessage());
            try {
                TextDB.conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println(
                        "Error during rollback: " + rollbackEx.getMessage());
            }
        } finally {
            // Close resources to prevent locking issues
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
            try {
                if (ps1 != null) {
                    ps1.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
            try {
                if (ps2 != null) {
                    ps2.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
        }
    }

    public static void executeReturnEquipment(String serialNumber,
            String updateStatusSQL) {
        PreparedStatement ps = null;
        try {
            ps = TextDB.conn.prepareStatement(updateStatusSQL);
            ps.setString(1, serialNumber);
            int equipmentUpdated = ps.executeUpdate();

            if (equipmentUpdated > 0) {
                System.out.println("Equipment returned successfully.");
            } else {
                System.out.println(
                        "Error: Equipment not found or already returned.");
            }

        } catch (SQLException e) {
            System.out.println("Error executing return: " + e.getMessage());
        } finally {
            // Close the PreparedStatement to prevent locking issues
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
        }
    }

     public static void executeFetchWarehouseForPickup(String serialNumber,
            String fetchWarehouseSQL) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = TextDB.conn.prepareStatement(fetchWarehouseSQL);
            ps.setString(1, serialNumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                String warehouseAddress = rs.getString("Address");
                String warehouseCity = rs.getString("City");
                System.out.println("Equipment can be picked up from:");
                System.out.println("Warehouse Address: " + warehouseAddress
                        + ", " + warehouseCity);
            } else {
                System.out.println(
                        "Error: Equipment or associated warehouse not found.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error fetching warehouse information: " + e.getMessage());
        } finally {
            // Close resources to prevent locking issues
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                /* Ignored */ }
        }
    }
     

     public static void ps_getTotalRentedItemsByMember(String sql, int userID) {
    	 try {
      		ps = TextDB.conn.prepareStatement(sql);
      		ps.setInt(1, userID);
      	} catch (SQLException e) {
              System.out.println(e.getMessage());
          }
      	
      	sqlQuery(TextDB.conn, ps);
     }

     public static void ps_getEquipmentByTypeAndYear(String sql, int year) {
    	 try {
     		ps = TextDB.conn.prepareStatement(sql);
     		ps.setInt(1, year);
     	} catch (SQLException e) {
             System.out.println(e.getMessage());
         }
     	
     	sqlQuery(TextDB.conn, ps);
     }
}
