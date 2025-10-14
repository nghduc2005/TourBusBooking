package com.csdl.tourbusbooking.entity;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Coach {
    private String coach_id;
    private String coach_type;
    private int total_seat;
    private String[] ordered_seat;
    private Integer price;
    private String trip_id;
}
