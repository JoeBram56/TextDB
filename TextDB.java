import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class TextDB {

    /*
     * Don't make instances of this class
     */
    private TextDB() {

    }

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

    // Lists to store drones and equipment
    private static ArrayList<Drone> drones = new ArrayList<Drone>();
    private static ArrayList<Equipment> equipmentList = new ArrayList<Equipment>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        int user;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rSet = null;

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
        scanner.close();
    }

    // Adds drone
    private static void addDrone(Scanner scanner) {
        System.out.print("Enter Serial Number: ");
        String serialNumber = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        System.out.print("Enter Status: ");
        String status = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Weight Capacity: ");
        double weightCapacity = scanner.nextDouble();
        drones.add(new Drone(serialNumber, name, model, status, location,
                weightCapacity));
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
    private static void rentEquipment(Scanner scanner) {
        System.out.println("Enter equipment details for renting.");
        System.out.println("Equipment rented.");
    }

    private static void returnEquipment(Scanner scanner) {
        System.out.println("Enter equipment details for returning.");
        System.out.println("Equipment returned.");
    }

    private static void deliverEquipment(Scanner scanner) {
        System.out.println("Enter equipment details for delivery.");
        System.out.println("Equipment delivered.");
    }

    private static void pickupEquipment(Scanner scanner) {
        System.out.println("Enter equipment details for pickup.");
        System.out.println("Equipment picked up.");
    }

    // Example: Search drones
    private static void searchDrones(Scanner scanner) {
        System.out.print("Enter Serial Number to search: ");
        String serialNumber = scanner.nextLine();
        for (Drone drone : drones) {
            if (drone.getSerialNumber().equals(serialNumber)) {
                drone.display();
                return;
            }
        }
        System.out.println("Drone not found.");
    }

    // Example: Edits a drone name (Assume full implementation will allow for
    //editing of all attributes
    private static void editDrone(Scanner scanner) {
        System.out.print("Enter Serial Number of the drone to edit: ");
        String serialNumber = scanner.nextLine();
        for (Drone drone : drones) {
            if (drone.getSerialNumber().equals(serialNumber)) {
                System.out.print("Enter new name: ");
                drone.setName(scanner.nextLine());
                System.out.println("Drone updated.");
                return;
            }
        }
        System.out.println("Drone not found.");
    }

    // Example: Delete a drone
    private static void deleteDrone(Scanner scanner) {
        System.out.print("Enter Serial Number of the drone to delete: ");
        String serialNumber = scanner.nextLine();
        for (int i = 0; i < drones.size(); i++) {
            if (drones.get(i).getSerialNumber().equals(serialNumber)) {
                drones.remove(i);
                System.out.println("Drone deleted.");
                return;
            }
        }
        System.out.println("Drone not found.");
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

}
