package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.entity.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @GetMapping
    public List<Account> getAllAccounts() {}
    @GetMapping("/{id}")
    public Account getAccounts(@PathVariable int id) {}
    @PostMapping
    public String addAccount(@RequestBody Account account) {}
    @PutMapping("/{id}")
    public String updateAccount(@PathVariable int id, @RequestBody Account account) {}
}
