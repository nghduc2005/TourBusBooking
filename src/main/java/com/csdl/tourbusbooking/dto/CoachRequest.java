package com.csdl.tourbusbooking.dto;

import lombok.Data;

@Data
public class CoachRequest {
    private String coach_name;
    private String coach_type;
    private int total_seat;
    private String status;
}
