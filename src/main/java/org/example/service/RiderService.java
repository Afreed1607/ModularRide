package org.example.service;

import org.example.model.Rider;
import org.example.model.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RiderService {
    private final Map<String, Rider> riders = new HashMap<>();

    public void registerRider(Rider rider) {
        if (riders.containsKey(rider.getId())) {
            throw new IllegalArgumentException("Rider with ID " + rider.getId() + " already exists.");
        }
        riders.put(rider.getId(), rider);
    }

    public Rider getRiderById(String riderId) {
        return riders.get(riderId);
    }

    public Optional<Rider> findRiderById(String riderId) {
        return Optional.ofNullable(riders.get(riderId));
    }

    public Map<String, Rider> getAllRiders() {
        return new HashMap<>(riders);
    }

    public void updateRiderLocation(String riderId, Location newLocation) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            throw new IllegalArgumentException("Rider with ID " + riderId + " not found.");
        }
        rider.setLocation(newLocation);
    }

    public boolean riderExists(String riderId) {
        return riders.containsKey(riderId);
    }
}

