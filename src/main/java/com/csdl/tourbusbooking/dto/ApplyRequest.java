package com.csdl.tourbusbooking.dto;

import lombok.Data;

@Data
public class ApplyRequest
{
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String position;
    private String experience;
    private String education_level;
    private String salary_expectation;
    private String cover_letter;
    private String resume;
}
