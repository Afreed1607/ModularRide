package org.example.service;

import org.example.model.Driver;
import org.example.model.Location;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DriverService {
    private final Map<String, Driver> drivers = new HashMap<>();

    public void registerDriver(Driver driver) {
        if (drivers.containsKey(driver.getId())) {
            throw new IllegalArgumentException("Driver with ID " + driver.getId() + " already exists.");
        }
        drivers.put(driver.getId(), driver);
    }

    public Driver getDriverById(String driverId) {
        return drivers.get(driverId);
    }

    public Optional<Driver> findDriverById(String driverId) {
        return Optional.ofNullable(drivers.get(driverId));
    }

    public void updateDriverAvailability(String driverId, boolean available) {
        Driver driver = drivers.get(driverId);
        if (driver == null) {
            throw new IllegalArgumentException("Driver with ID " + driverId + " not found.");
        }
        driver.setAvailable(available);
    }

    public void updateDriverLocation(String driverId, Location newLocation) {
        Driver driver = drivers.get(driverId);
        if (driver == null) {
            throw new IllegalArgumentException("Driver with ID " + driverId + " not found.");
        }
        driver.setCurrentLocation(newLocation);
    }

    public List<Driver> getAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .toList();
    }

    public List<Driver> getAllDrivers() {
        return List.copyOf(drivers.values());
    }

    public boolean driverExists(String driverId) {
        return drivers.containsKey(driverId);
    }
}

