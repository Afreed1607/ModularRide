package org.example.strategy.fare;

import org.example.model.Ride;
import java.time.LocalDateTime;

public class PeakHourFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 5.0;
    private static final double PRICE_PER_KM = 10.0;
    private static final double PEAK_HOUR_MULTIPLIER = 1.5;

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = BASE_FARE + (ride.getDistance() * PRICE_PER_KM);
        
        if (isPeakHour()) {
            baseFare *= PEAK_HOUR_MULTIPLIER;
        }
        
        return baseFare;
    }

    private boolean isPeakHour() {
        int hour = LocalDateTime.now().getHour();
        return (hour >= 7 && hour <= 9) || (hour >= 17 && hour <= 19);
    }
}

