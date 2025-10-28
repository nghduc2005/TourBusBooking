package com.csdl.tourbusbooking.model;

import java.time.LocalTime;

public class Ticket {
    private long ticket_id;
    private long account_id;
    private String ticket_type;
    private long total_price;
    private String payment_status;
    private LocalTime created_at;
    private String note;
    private long coach_id;
}
