package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.dto.*;
import com.csdl.tourbusbooking.model.Account;
import com.csdl.tourbusbooking.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if(!accountService.createAccount(request)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Lỗi tạo tài khoản!");
        }
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
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        if(session.getAttribute("account") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Đăng xuất thành công!");
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        String username = (String) session.getAttribute("account");
        Account account =  accountService.getProfile(username);
        if(account == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin tài khoản!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
    @GetMapping("/edit/{id}")
    public ResponseEntity<?> getProfile(@PathVariable String id) {
        Account account =  accountService.getProfileById(id);
        if(account == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin tài khoản!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, HttpSession session) {
        String username = (String) session.getAttribute("account");
        if(accountService.changePassword(request, username)) {
            return ResponseEntity.status(HttpStatus.OK).body("Thay đổi mật khẩu thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Thay đổi mật khẩu thất bại!");
        }
    }
    @PatchMapping("/profile")
    public ResponseEntity<?> changeProfile(@RequestBody ChangeProfileRequest request, HttpSession session) {
        String username = (String) session.getAttribute("account");
        if(accountService.changeProfile(request, username)) {
            return ResponseEntity.status(HttpStatus.OK).body("Thay đổi thông tin thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Thay đổi thông tin thất bại!");
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        Boolean checkIfAccountExist = accountService.findAccountByUsername(request.getUsername());
        if(checkIfAccountExist){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại!");
        }
        if(!accountService.createAccountAdmin(request)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Lỗi tạo tài khoản!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo tài khoản thành công!");
    }
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> editAccount(@PathVariable("id") int id, @RequestBody ChangeProfileRequest request) {
        Boolean checkIfAccountExist = accountService.findAccountById(id);
        if(!checkIfAccountExist){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản không tồn tại trong hệ thống!");
        }
        if(accountService.editAccountAdmin(request, id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thông tin tài khoản thành công!");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Lỗi cập nhật thông tin tài khoản!");
    }
    @PatchMapping("/change-password/{id}")
    public ResponseEntity<?> changePasswordId(@PathVariable("id") int id, @RequestBody ChangePasswordRequest request) {
        Boolean checkIfAccountExist = accountService.findAccountById(id);
        if(accountService.changePasswordById(request, id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Thay đổi mật khẩu thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Thay đổi mật khẩu thất bại!");
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteId(@PathVariable("id") int id) {
        if(accountService.deletedById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Xóa tài khoản thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Xóa tài khoản thất bại!");
        }
    }
    @DeleteMapping("/delete-selected")
    public ResponseEntity<?> deleteSelected(@RequestBody DeleteSelectedRequest request) {
        int rows = accountService.deleteByIds(request.getIds());
        if(rows == request.getIds().size()) {
            return ResponseEntity.status(HttpStatus.OK).body("Xóa tài khoản thành công!");
        } else if(rows < request.getIds().size() && rows > 0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Đã xóa "+ rows +" tài khoản thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Xóa tài khoản thất bại!");
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllAccounts(HttpSession session) {
        String username = (String) session.getAttribute("account");
        List<AccountResponse> accountList = accountService.getAllAccounts(username);
        return ResponseEntity.status(HttpStatus.OK).body(accountList);
    }
}
