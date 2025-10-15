package com.csdl.tourbusbooking.dto;

import lombok.Data;
@Data
public class RegisterRequest {
    private String phone;
    private String address;
    private String fullname;
    private String username;
    private String password;
}
