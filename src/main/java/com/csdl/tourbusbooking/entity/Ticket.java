package com.csdl.tourbusbooking.entity;

import java.time.LocalDateTime;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String ticket_id;
    private String account_id;
    private String name;
    private String phone;
    private String address;
    private int price;
    private String ticket_type;
    private String payment_status;
    private LocalDateTime created_time;
    private String note;
    private String start_location;
    private String end_location;
}
