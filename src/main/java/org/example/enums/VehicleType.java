package org.example.enums;

public enum VehicleType {
    BIKE(2),
    AUTO(3),
    CAR(4);

    private final int capacity;

    VehicleType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}

