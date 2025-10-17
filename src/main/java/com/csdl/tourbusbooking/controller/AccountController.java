package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.dto.RegisterRequest;
import com.csdl.tourbusbooking.entity.Account;
import com.csdl.tourbusbooking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Long> registerAccount(@RequestBody RegisterRequest request) {
        Account account = toEntity(request);
        Long id = accountService.register(account);
        return ResponseEntity.ok(id);
    }
    private Account toEntity(RegisterRequest r) {
        Account a = new Account();
        a.setUsername(r.getUsername());
        a.setPassword(r.getPassword());
        a.setFullname(r.getFullname());
        a.setAddress(r.getAddress());
        a.setPhone(r.getPhone());
        a.setRole("USER");
        return a;
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
