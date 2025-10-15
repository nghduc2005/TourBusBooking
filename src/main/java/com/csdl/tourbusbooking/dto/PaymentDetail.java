package com.csdl.tourbusbooking.dto;

import lombok.Data;

@Data
public class PaymentDetail {
    private String fullname;
    private String phone;
    private String address;
    private String note;
    private String[] ordered_seat;
}
