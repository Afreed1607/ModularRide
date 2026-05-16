package org.example;

import org.example.enums.VehicleType;
import org.example.model.Driver;
import org.example.model.Location;
import org.example.model.Ride;
import org.example.model.Rider;
import org.example.service.DriverService;
import org.example.service.RiderService;
import org.example.service.RideService;
import org.example.strategy.fare.DefaultFareStrategy;
import org.example.strategy.fare.FareStrategy;
import org.example.strategy.matching.NearestDriverStrategy;
import org.example.strategy.matching.RideMatchingStrategy;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RiderService riderService = new RiderService();
    private static final DriverService driverService = new DriverService();
    private static final FareStrategy fareStrategy = new DefaultFareStrategy();
    private static final RideMatchingStrategy matchingStrategy = new NearestDriverStrategy();
    private static final RideService rideService = new RideService(driverService, matchingStrategy, fareStrategy);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║        Welcome to RideWise Ride-Sharing System        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addRider();
                    case "2" -> addDriver();
                    case "3" -> viewAvailableDrivers();
                    case "4" -> requestRide();
                    case "5" -> completeRide();
                    case "6" -> viewRides();
                    case "7" -> {
                        running = false;
                        System.out.println("\nThank you for using RideWise! Goodbye!");
                    }
                    default -> System.out.println("\n❌ Invalid option. Please try again.\n");
                }
            } catch (Exception e) {
                System.out.println("\n❌ Error: " + e.getMessage() + "\n");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("                        MAIN MENU                          ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request a Ride");
        System.out.println("5. Complete a Ride");
        System.out.println("6. View All Rides");
        System.out.println("7. Exit");
        System.out.println("───────────────────────────────────────────────────────────");
        System.out.print("Enter your choice: ");
    }

    private static void addRider() {
        System.out.println("\n┌─ Add New Rider ─────────────────────────────────┐");

        System.out.print("Enter Rider Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Rider name cannot be empty.");
        }

        System.out.print("Enter Current Latitude: ");
        double latitude = parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Current Longitude: ");
        double longitude = parseDouble(scanner.nextLine().trim());

        String riderId = generateId("R");
        Location location = new Location(latitude, longitude);
        Rider rider = new Rider(riderId, name, location);

        riderService.registerRider(rider);
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.println("✅ Rider registered successfully!");
        System.out.println("   Rider ID: " + riderId);
        System.out.println("   Name: " + name);
        System.out.println("   Location: " + location + "\n");
    }

    private static void addDriver() {
        System.out.println("\n┌─ Add New Driver ────────────────────────────────┐");

        System.out.print("Enter Driver Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be empty.");
        }

        System.out.print("Enter Current Latitude: ");
        double latitude = parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Current Longitude: ");
        double longitude = parseDouble(scanner.nextLine().trim());

        System.out.println("Select Vehicle Type:");
        System.out.println("  1. BIKE (2 seats)");
        System.out.println("  2. AUTO (3 seats)");
        System.out.println("  3. CAR (4 seats)");
        System.out.print("Enter choice (1-3): ");

        VehicleType vehicleType = switch (scanner.nextLine().trim()) {
            case "1" -> VehicleType.BIKE;
            case "2" -> VehicleType.AUTO;
            case "3" -> VehicleType.CAR;
            default -> throw new IllegalArgumentException("Invalid vehicle type.");
        };

        String driverId = generateId("D");
        Location location = new Location(latitude, longitude);
        Driver driver = new Driver(driverId, name, location, vehicleType);

        driverService.registerDriver(driver);
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.println("✅ Driver registered successfully!");
        System.out.println("   Driver ID: " + driverId);
        System.out.println("   Name: " + name);
        System.out.println("   Vehicle Type: " + vehicleType);
        System.out.println("   Location: " + location + "\n");
    }

    private static void viewAvailableDrivers() {
        System.out.println("\n┌─ Available Drivers ─────────────────────────────┐");

        var availableDrivers = driverService.getAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            System.out.println("No available drivers at the moment.\n");
            return;
        }

        System.out.println("Available Drivers:\n");
        for (Driver driver : availableDrivers) {
            System.out.println("  Driver ID: " + driver.getId());
            System.out.println("  Name: " + driver.getName());
            System.out.println("  Vehicle Type: " + driver.getVehicleType());
            System.out.println("  Location: " + driver.getCurrentLocation());
            System.out.println("  ─────────────────────────────────");
        }
        System.out.println();
    }

    private static void requestRide() {
        System.out.println("\n┌─ Request a Ride ────────────────────────────────┐");

        System.out.print("Enter Rider ID: ");
        String riderId = scanner.nextLine().trim();

        if (!riderService.riderExists(riderId)) {
            throw new IllegalArgumentException("Rider with ID " + riderId + " not found.");
        }

        Rider rider = riderService.getRiderById(riderId);

        System.out.print("Enter Pickup Latitude: ");
        double pickupLat = parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Pickup Longitude: ");
        double pickupLon = parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Dropoff Latitude: ");
        double dropoffLat = parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Dropoff Longitude: ");
        double dropoffLon = parseDouble(scanner.nextLine().trim());

        Location pickupLocation = new Location(pickupLat, pickupLon);
        Location dropoffLocation = new Location(dropoffLat, dropoffLon);

        double distance = calculateDistance(pickupLocation, dropoffLocation);

        String rideId = generateId("RIDE");
        Ride ride = new Ride(rideId, rider, pickupLocation, dropoffLocation, distance);

        rideService.requestRide(rideId, rider, ride);

        try {
            rideService.assignDriver(rideId);
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.println("✅ Ride requested and driver assigned!");
            System.out.println("   Ride ID: " + rideId);
            System.out.println("   Rider: " + rider.getName());
            System.out.println("   Driver: " + ride.getDriver().getName());
            System.out.println("   Distance: " + String.format("%.2f", distance) + " km");
            System.out.println("   Status: " + ride.getStatus() + "\n");
        } catch (IllegalStateException e) {
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.println("⚠️  Ride created but no drivers available for assignment.");
            System.out.println("   Ride ID: " + rideId);
            System.out.println("   Status: " + ride.getStatus() + "\n");
        }
    }

    private static void completeRide() {
        System.out.println("\n┌─ Complete a Ride ───────────────────────────────┐");

        System.out.print("Enter Ride ID: ");
        String rideId = scanner.nextLine().trim();

        if (!rideService.findRideById(rideId).isPresent()) {
            throw new IllegalArgumentException("Ride with ID " + rideId + " not found.");
        }

        Ride ride = rideService.getRideById(rideId);

        if (ride.getDriver() == null) {
            throw new IllegalStateException("Cannot complete a ride without an assigned driver.");
        }

        var fareReceipt = rideService.completeRide(rideId);
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.println("✅ Ride completed successfully!");
        System.out.println("   Ride ID: " + rideId);
        System.out.println("   Rider: " + ride.getRider().getName());
        System.out.println("   Driver: " + ride.getDriver().getName());
        System.out.println("   Distance: " + String.format("%.2f", ride.getDistance()) + " km");
        System.out.println("   Fare Amount: $" + String.format("%.2f", fareReceipt.getAmount()));
        System.out.println("   Completed At: " + fareReceipt.getGeneratedAt() + "\n");
    }

    private static void viewRides() {
        System.out.println("\n┌─ All Rides ─────────────────────────────────────┐");

        var allRides = rideService.getAllRides();

        if (allRides.isEmpty()) {
            System.out.println("No rides found.\n");
            return;
        }

        System.out.println("All Rides:\n");
        for (Ride ride : allRides) {
            System.out.println("  Ride ID: " + ride.getId());
            System.out.println("  Rider: " + ride.getRider().getName());
            System.out.println("  Driver: " + (ride.getDriver() != null ? ride.getDriver().getName() : "Unassigned"));
            System.out.println("  Distance: " + String.format("%.2f", ride.getDistance()) + " km");
            System.out.println("  Status: " + ride.getStatus());
            System.out.println("  ─────────────────────────────────");
        }
        System.out.println();
    }

    private static double parseDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + input);
        }
    }

    private static double calculateDistance(Location loc1, Location loc2) {
        double lat1 = loc1.getLatitude();
        double lon1 = loc1.getLongitude();
        double lat2 = loc2.getLatitude();
        double lon2 = loc2.getLongitude();

        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;

        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }

    private static String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}