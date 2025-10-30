package com.csdl.tourbusbooking.model;

import lombok.Data;

@Data
public class Account {
    private int account_id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String role;
    private String[] order_history;
}
