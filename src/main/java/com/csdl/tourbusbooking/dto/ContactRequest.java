package com.csdl.tourbusbooking.dto;

import lombok.Data;

@Data
public class ContactRequest {
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String subject;
    private String message;
}
