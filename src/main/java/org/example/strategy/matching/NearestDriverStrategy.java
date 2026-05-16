package org.example.strategy.matching;

import org.example.model.Driver;
import org.example.model.Location;
import org.example.model.Rider;
import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        if (drivers.isEmpty()) {
            return null;
        }

        return drivers.stream()
                .filter(Driver::isAvailable)
                .min((d1, d2) -> Double.compare(
                        calculateDistance(rider.getLocation(), d1.getCurrentLocation()),
                        calculateDistance(rider.getLocation(), d2.getCurrentLocation())
                ))
                .orElse(null);
    }

    private double calculateDistance(Location loc1, Location loc2) {
        double lat1 = loc1.getLatitude();
        double lon1 = loc1.getLongitude();
        double lat2 = loc2.getLatitude();
        double lon2 = loc2.getLongitude();

        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;

        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
}

