package org.example.model;

import org.example.enums.RideStatus;

public class Ride {
    private String id;
    private Rider rider;
    private Driver driver;
    private double distance;
    private RideStatus status;
    private Location pickupLocation;
    private Location dropoffLocation;

    public Ride(String id, Rider rider, Location pickupLocation, Location dropoffLocation, double distance) {
        this.id = id;
        this.rider = rider;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.distance = distance;
        this.status = RideStatus.REQUESTED;
        this.driver = null;
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getDistance() {
        return distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id='" + id + '\'' +
                ", rider=" + rider.getName() +
                ", driver=" + (driver != null ? driver.getName() : "Unassigned") +
                ", distance=" + distance +
                ", status=" + status +
                ", pickup=" + pickupLocation +
                ", dropoff=" + dropoffLocation +
                '}';
    }
}

