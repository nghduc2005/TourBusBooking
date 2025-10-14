package com.csdl.tourbusbooking.entity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private String trip_id;
    private String trip_name;
    private String start_location;
    private String end_location;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private int cost;
    private String status;
}