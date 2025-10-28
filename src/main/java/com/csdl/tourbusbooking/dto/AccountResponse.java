package com.csdl.tourbusbooking.dto;

import lombok.Data;

@Data
public class AccountResponse {
    private int account_id;
    private String name;
    private String username;
    private String role;
    private String phone;
    private String address;
}
