package com.example.locationtracker.consumer;

import com.example.locationtracker.model.Location;
import com.example.locationtracker.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocationConsumer {
    private ReportService reportService;

    public LocationConsumer(ReportService reportService) {
        this.reportService = reportService;
    }

    @KafkaListener(topics = "location-topic", groupId = "location-group")
    public void listen(Location location) {
        reportService.addLocation(location);
        log.info("Received location: {}", location);
    }
}
