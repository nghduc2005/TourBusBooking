package com.csdl.tourbusbooking.dto;

import lombok.Data;

import java.time.Instant;
@Data
public class TripResponse {
    private int trip_id;
    private String start_location;
    private String end_location;
    private Instant start_time;
    private int price;
    private String status;
    private String coach_type;
    private int coach_id;
    private int total_seat;
}
