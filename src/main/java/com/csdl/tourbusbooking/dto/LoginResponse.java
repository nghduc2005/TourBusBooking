package com.csdl.tourbusbooking.dto;

import com.csdl.tourbusbooking.model.Account;
import lombok.Data;

@Data
public class LoginResponse {
    Account body;
    String message;
    public LoginResponse(Account body, String message) {
        this.body = body;
        this.message = message;
    }
}
