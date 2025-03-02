package com.example.locationtracker.producer;

import com.example.locationtracker.model.Location;
import com.example.locationtracker.util.LocationGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocationProducer {
    private KafkaTemplate<String, Location> kafkaTemplate;
    private LocationGenerator locationGenerator;

    public LocationProducer(KafkaTemplate<String, Location> kafkaTemplate, LocationGenerator locationGenerator) {
        this.kafkaTemplate = kafkaTemplate;
        this.locationGenerator = locationGenerator;
    }

    @Scheduled(fixedRate = 5000)
    public void sendLocation() {
        Location location = locationGenerator.generateRandomLocation();
        kafkaTemplate.send("location-topic", location);
        log.info("Sent location: {}", location);
    }
}
