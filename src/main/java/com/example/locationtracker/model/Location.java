package com.example.locationtracker.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class Location implements Serializable {
    private double longitude;
    private double latitude;
    private long timestamp;
}
