import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextDB {

    /*
     * Don't make instances of this class
     */
    private TextDB() {

    }

    private static String DATABASE = "DatabaseProject.db";

    public static Connection conn = null;

    //Holds drone data
    static class Drone {
        private String serialNumber;
        private String name;
        private String model;
        private String status;
        private String location;
        private double weightCapacity;

        public Drone(String serialNumber, String name, String model,
                String status, String location, double weightCapacity) {
            this.serialNumber = serialNumber;
            this.name = name;
            this.model = model;
            this.status = status;
            this.location = location;
            this.weightCapacity = weightCapacity;
        }

        public String getSerialNumber() {
            return this.serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void display() {
            System.out.println("Serial Number: " + this.serialNumber
                    + ", Name: " + this.name + ", Model: " + this.model
                    + ", Status: " + this.status + ", Location: "
                    + this.location + ", Weight Capacity: "
                    + this.weightCapacity);
        }
    }

    // Nested Equipment class
    static class Equipment {
        private String type;
        private String description;
        private String model;
        private String serialNumber;
        private String location;

        public Equipment(String type, String description, String model,
                String serialNumber, String location) {
            this.type = type;
            this.description = description;
            this.model = model;
            this.serialNumber = serialNumber;
            this.location = location;
        }

        public String getSerialNumber() {
            return this.serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void display() {
            System.out.println("Type: " + this.type + ", Description: "
                    + this.description + ", Model: " + this.model
                    + ", Serial Number: " + this.serialNumber + ", Location: "
                    + this.location);
        }
    }

    /**
     * Connects to the database if it exists, creates it if it does not, and
     * returns the connection object.
     *
     * @param databaseFileName
     *            the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {
        /**
         * The "Connection String" or "Connection URL".
         *
         * "jdbc:sqlite:" is the "subprotocol". (If this were a SQL Server
         * database it would be "jdbc:sqlserver:".)
         */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                // Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
                System.out
                        .println("The driver name is " + meta.getDriverName());
                System.out.println(
                        "The connection to the database was successful.");
            } else {
                // Provides some feedback in case the connection failed but did not throw an exception.
                System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out
                    .println("There was a problem connecting to the database.");
        }
        return conn;
    }

    // Lists to store drones and equipment
    private static ArrayList<Drone> drones = new ArrayList<Drone>();
    private static ArrayList<Equipment> equipmentList = new ArrayList<Equipment>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        int user;
        conn = initializeDB(DATABASE);

        System.out.print("Are you a customer or Employee?(1 - Emp/2 - Cust): ");
        user = scanner.nextInt();
        scanner.nextLine();

        if (user == 1) {

            while (choice != 10) {
                System.out.println("\n--- Menu ---");
                System.out.println("1. Add a new Drone");
                System.out.println("2. Edit Drone");
                System.out.println("3. Delete Drone");
                System.out.println("4. Search Drones");
                System.out.println("5. Add new Equipment");
                System.out.println("6. Edit Equipment");
                System.out.println("7. Delete Equipment");
                System.out.println("8. Search Equipment");
                System.out.println("9. Exit");
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        addDrone(scanner);
                        break;
                    case 2:
                        editDrone(scanner);
                        break;
                    case 3:
                        deleteDrone(scanner);
                        break;
                    case 4:
                        searchDrones(scanner);
                        break;
                    case 5:
                        addEquipment(scanner);
                        break;
                    case 6:
                        editEquipment(scanner);
                        break;
                    case 7:
                        deleteEquipment(scanner);
                        break;
                    case 8:
                        searchEquipment(scanner);
                    case 9:
                        System.out.println("Exiting program.");
                        return;
                    //break;
                    default:
                        break;
                }

            }
        } else if (user == 2) {

            while (choice != 10) {
                System.out.println("\n--- Menu ---");
                System.out.println("1. Rent Equipment");
                System.out.println("2. Return Equipment");
                System.out.println("3. Deliver Equipment");
                System.out.println("4. Pickup Equipment");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        rentEquipment(scanner);
                        break;
                    case 2:
                        returnEquipment(scanner);
                        break;
                    case 3:
                        deliverEquipment(scanner);
                        break;
                    case 4:
                        pickupEquipment(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting program.");
                        return;
                    //break;
                    default:
                        break;
                }
            }

        } else {
            System.out.println("Need valid input");
            scanner.close();
            return;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        scanner.close();
    }

    // Adds drone
    private static void addDrone(Scanner scanner) {
        System.out.print("Enter Serial Number: ");
        int serialNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        System.out
                .print("Enter Status (True-Available/False - Not Available): ");
        boolean status = Boolean.getBoolean(scanner.nextLine());
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Manufacturer: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter weight capacity: ");
        double weightCapacity = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Distance: ");
        int distance = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter MaxSpeed: ");
        int maxSpeed = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Warranty Expiration: ");
        String warrantyExpiration = scanner.nextLine();

        String sql = "insert into Drones (DSerialNumber, Name, Model, Status, Location, Manufacturer, Year, WeightCapacity, Distance, MaxSpeed, WarrantyExpiration) values (?,?,?,?,?,?,?,?,?,?,?)";
        SQL.ps_add_drone(sql, serialNumber, name, model, status, location,
                manufacturer, year, weightCapacity, distance, maxSpeed,
                warrantyExpiration);
        System.out.println("Drone added.");
    }

    // Method to add equipment
    private static void addEquipment(Scanner scanner) {
        System.out.print("Enter Equipment Type: ");
        String type = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        System.out.print("Enter Serial Number: ");
        String serialNumber = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        equipmentList.add(new Equipment(type, description, model, serialNumber,
                location));
        System.out.println("Equipment added.");
    }

    // Placeholder methods for rental, delivery, etc.
    /*
     * TODO Alex/Arjun implement these methods for customers and using the
     * methods in SQL.java to help
     */
    private static void rentEquipment(Scanner scanner) {
        System.out.print("Enter your user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter equipment serial number to rent: ");
        String serialNumber = scanner.nextLine();

        String updateStatusSQL = "UPDATE Equipment SET Status = True WHERE ESerialNumber = ?";
        String insertRentalSQL = "INSERT INTO RentedBy (UserID, ESerialNumber, DueDate, CheckOutDate, RentalFee) VALUES (?, ?, ?, ?, ?)";

        SQL.executeRentEquipment(serialNumber, userId, updateStatusSQL,
                insertRentalSQL);
    }

    private static void returnEquipment(Scanner scanner) {
        System.out.println("Enter equipment serial number for return:");
        String serialNumber = scanner.nextLine();

        // SQL to update equipment status to returned (false)
        String updateStatusSQL = "UPDATE Equipment SET Status = False WHERE ESerialNumber = ?";

        SQL.executeReturnEquipment(serialNumber, updateStatusSQL);
    }

    private static void deliverEquipment(Scanner scanner) {
        System.out.print("Enter warehouse ID for delivery: ");
        int warehouseId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter equipment serial number to deliver: ");
        String serialNumber = scanner.nextLine();

        // SQL to fetch warehouse address for confirmation message
        String fetchWarehouseSQL = "SELECT Address, City FROM Warehouse WHERE WarehouseID = ?";
        String updateStatusSQL = "UPDATE Equipment SET Status = True, Location = ? WHERE ESerialNumber = ?";

        SQL.executeDeliverEquipment(warehouseId, serialNumber,
                fetchWarehouseSQL, updateStatusSQL);
    }

    private static void pickupEquipment(Scanner scanner) {
        System.out.println(
                "Enter equipment serial number to view pickup warehouse:");
        String serialNumber = scanner.nextLine();

        // SQL to fetch warehouse address for the specified equipment serial number
        String fetchWarehouseSQL = "SELECT W.Address, W.City FROM Warehouse W JOIN Equipment E ON E.Location = W.WarehouseID WHERE E.ESerialNumber = ?";

        SQL.executeFetchWarehouseForPickup(serialNumber, fetchWarehouseSQL);
    }
    // Example: Search drones
    private static void searchDrones(Scanner scanner) {
        System.out.print("Enter Serial Number to search: ");
        int serialNumber = Integer.parseInt(scanner.nextLine());

        String sql = "select * from Drones where DSerialNumber = ?";
        SQL.ps_search_drone(sql, serialNumber);

    }

    // Example: Edits a drone name (Assume full implementation will allow for
    //editing of all attributes
    private static void editDrone(Scanner scanner) {
        System.out.print("Enter Serial Number of the drone to change status: ");
        int oldSerialNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter the new serial number: ");
        int newNum = Integer.parseInt(scanner.nextLine());

        String sql = "Update Drones set DSerialNumber = ? where DSerialNumber = ?;";
        SQL.ps_update_drone(sql, oldSerialNumber, newNum);
        System.out.println("Drone updated");

    }

    // Example: Delete a drone
    private static void deleteDrone(Scanner scanner) {
        System.out.print("Enter Serial Number of the drone to delete: ");
        int serialNumber = Integer.parseInt(scanner.nextLine());

        String sql = "Delete from Drones where DSerialNumber = ?;";
        SQL.ps_delete_drone(sql, serialNumber);
    }

    private static void searchEquipment(Scanner scanner) {
        System.out.print("Enter Serial Number to search: ");
        String serialNumber = scanner.nextLine();
        for (Equipment e : equipmentList) {
            if (e.getSerialNumber().equals(serialNumber)) {
                e.display();
                return;
            }
        }
        System.out.println("Equipment not found.");
    }

    private static void editEquipment(Scanner scanner) {
        System.out.print("Enter Serial Number of the Equipment to edit: ");
        String serialNumber = scanner.nextLine();
        for (Equipment e : equipmentList) {
            if (e.getSerialNumber().equals(serialNumber)) {
                System.out.print("Enter new type: ");
                e.setType(scanner.nextLine());
                System.out.println("Equipment updated.");
                return;
            }
        }
        System.out.println("Equipment not found.");
    }

    private static void deleteEquipment(Scanner scanner) {
        System.out.print("Enter Serial Number of the Equipment to delete: ");
        String serialNumber = scanner.nextLine();
        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipmentList.get(i).getSerialNumber().equals(serialNumber)) {
                equipmentList.remove(i);
                System.out.println("Equipment deleted.");
                return;
            }
        }
        System.out.println("Equipment not found.");
    }

    /*
     * TODO Kyle: Do those reports by calling/adding methods in sql.java. You
     * might need to add another menu option for the reports too
     */

}
