package com.csdl.tourbusbooking.dto;

import com.csdl.tourbusbooking.model.OutBound;
import com.csdl.tourbusbooking.model.ReturnTrip;
import lombok.Data;

import java.time.Instant;

@Data
public class TicketResponse {
    //Thôn tin vé
    private int ticket_id;
    private int account_id;
    private String name;
    private String address;
    private String phone;
    private String ticket_type;
    private int total_price;
    private String payment_status;
    private Instant created_time;
    private String note;
    private String start_location;
    private String end_location;
    private OutBound outbound;
    private ReturnTrip round;
}
