package com.csdl.tourbusbooking.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class TripRequest {
    private String start_location;
    private String end_location;
    private Instant start_time;
    private int price;
    private int coach_id;
}
