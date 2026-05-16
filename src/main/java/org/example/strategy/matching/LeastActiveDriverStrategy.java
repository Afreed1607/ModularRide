package org.example.strategy.matching;

import org.example.model.Driver;
import org.example.model.Rider;
import java.util.List;
import java.util.Random;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        if (drivers.isEmpty()) {
            return null;
        }

        List<Driver> availableDrivers = drivers.stream()
                .filter(Driver::isAvailable)
                .toList();

        if (availableDrivers.isEmpty()) {
            return null;
        }

        // For simplicity, picking a random available driver
        // In production, we'd track ride counts per driver
        Random random = new Random();
        return availableDrivers.get(random.nextInt(availableDrivers.size()));
    }
}

