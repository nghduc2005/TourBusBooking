package com.csdl.tourbusbooking.model;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class OutBound {
    private int trip_id;
    private Instant start_time;
    private String coach_type;
    private int coach_id;
    private List<String> ordered_seat;
    private int price;
}
