package com.csdl.tourbusbooking.dto;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String name;
    private String username;
    private String password;
    private String phone;
    private String role;
    private String address;
    private String note;
}
