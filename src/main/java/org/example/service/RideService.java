package org.example.service;

import org.example.enums.RideStatus;
import org.example.model.FareReceipt;
import org.example.model.Ride;
import org.example.model.Rider;
import org.example.strategy.fare.FareStrategy;
import org.example.strategy.matching.RideMatchingStrategy;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RideService {
    private final Map<String, Ride> rides = new HashMap<>();
    private final DriverService driverService;
    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;

    public RideService(DriverService driverService, RideMatchingStrategy matchingStrategy, FareStrategy fareStrategy) {
        this.driverService = driverService;
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(String rideId, Rider rider, Ride ride) {
        if (rides.containsKey(rideId)) {
            throw new IllegalArgumentException("Ride with ID " + rideId + " already exists.");
        }
        rides.put(rideId, ride);
        return ride;
    }

    public void assignDriver(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new IllegalArgumentException("Ride with ID " + rideId + " not found.");
        }

        if (ride.getDriver() != null) {
            throw new IllegalStateException("Ride already has a driver assigned.");
        }

        List<DriverService> availableDriversList = driverService.getAvailableDrivers().stream()
                .map(driver -> driverService)
                .toList();

        var driver = matchingStrategy.findDriver(ride.getRider(), driverService.getAvailableDrivers());

        if (driver == null) {
            throw new IllegalStateException("No available drivers found for ride.");
        }

        ride.setDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);
        driverService.updateDriverAvailability(driver.getId(), false);
    }

    public FareReceipt completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new IllegalArgumentException("Ride with ID " + rideId + " not found.");
        }

        if (ride.getDriver() == null) {
            throw new IllegalStateException("Cannot complete a ride without an assigned driver.");
        }

        ride.setStatus(RideStatus.COMPLETED);
        driverService.updateDriverAvailability(ride.getDriver().getId(), true);

        double fare = fareStrategy.calculateFare(ride);
        return new FareReceipt(rideId, fare, LocalDateTime.now());
    }

    public void cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new IllegalArgumentException("Ride with ID " + rideId + " not found.");
        }

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed ride.");
        }

        ride.setStatus(RideStatus.CANCELLED);

        if (ride.getDriver() != null) {
            driverService.updateDriverAvailability(ride.getDriver().getId(), true);
        }
    }

    public Ride getRideById(String rideId) {
        return rides.get(rideId);
    }

    public Optional<Ride> findRideById(String rideId) {
        return Optional.ofNullable(rides.get(rideId));
    }

    public List<Ride> getAllRides() {
        return List.copyOf(rides.values());
    }

    public List<Ride> getRidesByRider(String riderId) {
        return rides.values().stream()
                .filter(ride -> ride.getRider().getId().equals(riderId))
                .toList();
    }

    public List<Ride> getRidesByDriver(String driverId) {
        return rides.values().stream()
                .filter(ride -> ride.getDriver() != null && ride.getDriver().getId().equals(driverId))
                .toList();
    }
}

