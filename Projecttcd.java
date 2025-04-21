import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Vehicle class representing a general vehicle
class Vehicle {
    private String vehicleNo;
    private String brand;
    private String model;
    private double basePph; // Base price per hour
    private boolean isAvailable; // Availability status

    // Constructor to initialize vehicle details
    public Vehicle(String vehicleNo, String brand, String model, double basePph) {
        this.vehicleNo = vehicleNo;
        this.brand = brand;
        this.model = model;
        this.basePph = basePph;
        this.isAvailable = true; // Default availability set to true
    }

    // Getter for vehicle number
    public String getVehicleNo() {
        return vehicleNo;
    }

    // Getter for vehicle brand
    public String getBrand() {
        return brand;
    }

    // Getter for vehicle model
    public String getModel() {
        return model;
    }

    // Method to calculate rental price based on hours
    public double price(int hours) {
        return basePph * hours;
    }

    // Method to check if vehicle is available
    public boolean isAvailable() {
        return isAvailable;
    }

    // Method to mark vehicle as rented
    public void rent() {
        isAvailable = false;
    }

    // Method to mark vehicle as returned
    public void returnVehicle() {
        isAvailable = true;
    }
}

// Bike class representing a bike in the rental system, inheriting from Vehicle
class Bike extends Vehicle {
    // Constructor to initialize bike details
    public Bike(String bikeno, String brand, String model, double basePph) {
        super(bikeno, brand, model, basePph); // Call the superclass constructor
    }
}

// Customer class representing a customer
class Customer {
    private String id;
    private String name;

    // Constructor to initialize customer details
    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter for customer ID
    public String getId() {
        return id;
    }

    // Getter for customer name
    public String getName() {
        return name;
    }
}

// Rental class representing a rental transaction
class Rental {
    private Vehicle v;
    private Customer c;
    private int hours;

    // Constructor to initialize rental details
    public Rental(Vehicle v, Customer c, int hours) {
        this.v = v;
        this.c = c;
        this.hours = hours;
    }

    // Getter for vehicle involved in rental
    public Vehicle getVehicle() {
        return v;
    }

    // Getter for customer involved in rental
    public Customer getCustomer() {
        return c;
    }

    // Getter for rental duration in hours
    public int getHours() {
        return hours;
    }
}

// BikeRentals class managing the bike rental system
class BikeRentals {
    private List<Vehicle> bikes;
    private List<Customer> customers;
    private List<Rental> rentals;

    // Constructor to initialize lists
    public BikeRentals() {
        bikes = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    // Method to add a bike to the system
    public void addBike(Bike b) {
        bikes.add(b);
    }

    // Method to add a customer to the system
    public void addCustomer(Customer c) {
        customers.add(c);
    }

    // Method to rent a bike
    public void rentBike(Vehicle b, Customer c, int hours) {
        if (b.isAvailable()) {
            b.rent();
            rentals.add(new Rental(b, c, hours));
        } else {
            System.out.println("Bike not available");
        }
    }

    // Method to return a bike
    public void returnBike(Vehicle b) {
        b.returnVehicle();
        Rental rentalToRemove = null;
        for (Rental r : rentals) {
            if (r.getVehicle() == b) {
                rentalToRemove = r;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Bike was not rented");
        }
    }

    // Menu method to interact with the user
    public void menu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("====== BIKE RENTAL SYSTEM ======");
            System.out.println("1. Rent a bike");
            System.out.println("2. Return a bike");
            System.out.println("3. Exit");
            System.out.println("--------------------------------");
            System.out.println("Enter a choice:");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline
            if (choice == 1) {
                System.out.println("\n== Rent a bike ==\n");
                System.out.println("Enter your name:");
                String customerName = sc.nextLine();
                System.out.println("\nAvailable bikes:");
                for (Vehicle b : bikes) {
                    if (b.isAvailable()) {
                        System.out.println(b.getVehicleNo() + " - " + b.getBrand() + " - " + b.getModel());
                    }
                }
                System.out.println("\nEnter the bike no you want to rent:");
                String bikeNo = sc.nextLine();
                System.out.println("Enter number of hours:");
                int rentHours = sc.nextInt();
                sc.nextLine(); // Consume newline
                Customer newCustomer = new Customer("C" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);
                Vehicle selectedBike = null;
                for (Vehicle b : bikes) {
                    if (b.getVehicleNo().equals(bikeNo) && b.isAvailable()) {
                        selectedBike = b;
                        break;
                    }
                }
                if (selectedBike != null) {
                    double totalPrice = selectedBike.price(rentHours);
                    System.out.println("\n=== INVOICE ===\n");
                    System.out.println("Customer ID: " + newCustomer.getId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Bike: " + selectedBike.getBrand() + " " + selectedBike.getModel());
                    System.out.println("Rental Hours: " + rentHours);
                    System.out.println("Total Price: " + totalPrice);
                    rentBike(selectedBike, newCustomer, rentHours);
                    System.out.println("\nRented Successfully.");
                } else {
                    System.out.println("\nInvalid selection.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a bike ==\n");
                System.out.println("Enter the bike no of the bike:");
                String bikeNo = sc.nextLine();
                Vehicle bikeToReturn = null;
                for (Vehicle b : bikes) {
                    if (b.getVehicleNo().equals(bikeNo) && !b.isAvailable()) {
                        bikeToReturn = b;
                        break;
                    }
                }
                if (bikeToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getVehicle() == bikeToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }
                    if (customer != null) {
                        returnBike(bikeToReturn);
                        System.out.println("Bike returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Rental information missing.");
                    }
                } else {
                    System.out.println("Invalid bike number or bike not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }
}

// Main class to run the program
public class Projecttcd {
    public static void main(String[] args) {
        BikeRentals rentals = new BikeRentals();
        Bike b1 = new Bike("b00A", "Honda", "Activa", 30);
        Bike b2 = new Bike("b00B", "RE", "GT650", 69);
        Bike b3 = new Bike("b00C", "Yamaha", "Dio", 35);
        rentals.addBike(b1);
        rentals.addBike(b2);
        rentals.addBike(b3);
        rentals.menu();
    }
}
