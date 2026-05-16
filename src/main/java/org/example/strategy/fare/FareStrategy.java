package org.example.strategy.fare;

import org.example.model.Ride;

public interface FareStrategy {
    double calculateFare(Ride ride);
}

