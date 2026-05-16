package org.example.strategy.matching;

import org.example.model.Driver;
import org.example.model.Rider;
import java.util.List;

public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, List<Driver> drivers);
}

