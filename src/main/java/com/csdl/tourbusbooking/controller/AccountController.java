package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.dto.LoginRequest;
import com.csdl.tourbusbooking.dto.RegisterRequest;
import com.csdl.tourbusbooking.model.Account;
import com.csdl.tourbusbooking.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<String> registerAccount(@RequestBody RegisterRequest request) {
        Boolean checkIfAccountExist = accountService.findAccountByUsername(request.getUsername());
        if(checkIfAccountExist){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại!");
        }
        accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo tài khoản thành công!");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        Account account = accountService.findAndReturnAccount(request.getUsername());
        if(account == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại!");
        }
        if(!account.getPassword().equals(request.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu không chính xác!");
        }
        session.setAttribute("account", account.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("Login thành công!"); //tự gắn sessionId vào res qua cookie
    }
    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpSession session) {
        if(session.getAttribute("account") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Đăng xuất thành công!");
    }
//    @GetMapping
//    public List<Account> getAllAccounts() {}
//    @GetMapping("/{id}")
//    public Account getAccounts(@PathVariable int id) {}
//    @PostMapping
//    public String addAccount(@RequestBody Account account) {}
//    @PutMapping("/{id}")
//    public String updateAccount(@PathVariable int id, @RequestBody Account account) {}
}
