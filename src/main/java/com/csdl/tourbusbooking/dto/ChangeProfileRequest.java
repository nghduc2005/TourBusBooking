package com.csdl.tourbusbooking.dto;

import lombok.Data;

@Data
public class ChangeProfileRequest {
    private String name;
    private String phone;
    private String address;
    private String note;
}
