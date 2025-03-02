package com.example.locationtracker.service;

import com.example.locationtracker.model.Location;
import com.example.locationtracker.util.HaversineCalculator;
import com.squareup.moshi.Moshi;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ReportService {
    private RedisTemplate<String, String> redisTemplate;
    private HaversineCalculator haversineCalculator;
    private Moshi moshi;
    private static final String LOCATION_KEY = "locations";

    public ReportService(RedisTemplate<String, String> redisTemplate, HaversineCalculator haversineCalculator, Moshi moshi) {
        this.redisTemplate = redisTemplate;
        this.haversineCalculator = haversineCalculator;
        this.moshi = moshi;
    }

    @PostConstruct
    public void init() {
        redisTemplate.delete(LOCATION_KEY);
        log.info("Cleared Redis key '{}' on application startup", LOCATION_KEY);
    }

    public void addLocation(Location location) {
        try {
            String locationJson = moshi.adapter(Location.class).toJson(location);
            redisTemplate.opsForZSet().add(LOCATION_KEY, locationJson, location.getTimestamp());
            log.info("Stored location in Redis: {}", location);
            redisTemplate.opsForZSet().removeRangeByScore(LOCATION_KEY, 0, System.currentTimeMillis() - 3600000);
            Set<String> currentLocations = redisTemplate.opsForZSet().range(LOCATION_KEY, 0, -1);
            log.info("Current locations in Redis: {} (size: {})", currentLocations, currentLocations.size());
            generateReport();
        } catch (Exception e) {
            log.error("Failed to serialize location: {}", location, e);
        }
    }

    public void generateReport() {
        Set<String> locationJsons = redisTemplate.opsForZSet().range(LOCATION_KEY, 0, -1);
        if (locationJsons == null || locationJsons.size() < 2) {
            log.warn("Not enough data for report");
            return;
        }

        List<Location> locations = new ArrayList<>();
        for (String json : locationJsons) {
            try {
                Location loc = moshi.adapter(Location.class).fromJson(json);
                if (loc != null) {
                    locations.add(loc);
                }
            } catch (IOException e) {
                log.error("Failed to deserialize location: {}", json, e);
            }
        }

        if (locations.size() < 2) {
            log.warn("Not enough valid locations for report");
            return;
        }

        double totalDistance = 0.0;
        for (int i = 1; i < locations.size(); i++) {
            double segmentDistance = haversineCalculator.calculateDistance(
                    locations.get(i - 1).getLatitude(), locations.get(i - 1).getLongitude(),
                    locations.get(i).getLatitude(), locations.get(i).getLongitude()
            );
            totalDistance += totalDistance += segmentDistance;
            log.info("Distance between {} ({}) and {} ({}): {} km, Running total: {} km",
                    i - 1, locations.get(i - 1), i, locations.get(i), String.format("%.2f", segmentDistance), String.format("%.2f", totalDistance));
        }
        log.info("Total distance traveled: {} km (based on {} locations)", String.format("%.2f", totalDistance), locations.size());
    }
}
