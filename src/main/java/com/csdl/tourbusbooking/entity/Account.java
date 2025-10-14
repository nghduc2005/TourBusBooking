package com.csdl.tourbusbooking.entity;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String name;
    private String address;
    private String phone;
    private String username;
    private String password;
    private String note;
    private String role;
    private String[] order_history;
    private String account_id;
}
