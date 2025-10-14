package com.csdl.tourbusbooking.entity;

import java.time.LocalDateTime;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Return {
    private String trip_id;
    private String coach_id;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private String coach_type;
    private String[] ordered_seat;
}
