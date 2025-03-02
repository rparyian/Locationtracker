package com.example.locationtracker.util;

import com.example.locationtracker.model.Location;
import org.springframework.stereotype.Component;

import java.util.Random;
import javax.inject.Provider;

@Component
public class LocationGenerator {
    private final Random random;
    private final Provider<Location> locationProvider;

    public LocationGenerator(Random random, Provider<Location> locationProvider) {
        this.random = random;
        this.locationProvider = locationProvider;
    }

    public Location generateRandomLocation() {
        Location location = locationProvider.get(); // Новый экземпляр от Spring
        location.setLongitude(random.nextDouble() * 360 - 180); // -180 до 180
        location.setLatitude(random.nextDouble() * 180 - 90);   // -90 до 90
        location.setTimestamp(System.currentTimeMillis());
        return location;
    }
}
