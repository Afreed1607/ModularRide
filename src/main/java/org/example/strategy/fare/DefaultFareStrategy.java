package org.example.strategy.fare;

import org.example.model.Ride;

public class DefaultFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 5.0;
    private static final double PRICE_PER_KM = 10.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + (ride.getDistance() * PRICE_PER_KM);
    }
}

