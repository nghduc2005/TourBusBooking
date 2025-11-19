package com.csdl.tourbusbooking.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class TripRequest {
    private String trip_id;
    private String start_location;
    private String end_location;
    private Instant start_time;
    private int price;
    private int coach_id;
    private String status;
    private String coach_type;
    private int total_seat;
    private String[] ordered_seat;
}
