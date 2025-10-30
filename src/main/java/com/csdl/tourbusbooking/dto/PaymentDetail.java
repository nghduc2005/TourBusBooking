package com.csdl.tourbusbooking.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class PaymentDetail {
    //Thuộc tính accounts
    private String name;
    private String phone;
    private String address;
    private String note;
    //Thuộc tính trip
    private String start_location;
    private String end_location;
    private Instant start_time;
    private int trip_id;
    private int price;
    //Thuộc tính coach
    private String coach_type;
    private String coach_name;
    //Thuộc tính ticket
    private String[] ordered_seat;
    private int total_price;
}
