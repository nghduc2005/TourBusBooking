package com.csdl.tourbusbooking.entity;
import com.csdl.tourbusbooking.constant.UserConstant;
import com.csdl.tourbusbooking.dto.RegisterRequest;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String fullname;
    private String address;
    private String phone;
    private String username;
    private String password;
    private String role;
    private String[] order_history;
    private String account_id;
    Account (RegisterRequest request) {
        this.fullname = request.getFullname();
        this.address = request.getAddress();
        this.phone = request.getPhone();
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.role = UserConstant.CUSTOMER_ROLE;
    }
}
