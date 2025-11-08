package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.dto.*;
import com.csdl.tourbusbooking.model.Account;
import com.csdl.tourbusbooking.service.CoachService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
public class CoachController {
    @Autowired
    private CoachService coachService;

    @PostMapping("/create")
    public ResponseEntity<?> createCoach(@RequestBody CoachRequest request) {
        if(coachService.create(request)) {
            return ResponseEntity.status(HttpStatus.OK).body("Tạo xe thành công!");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Tạo xe thất bại!");
    }
    @GetMapping("/edit/{id}")
    public ResponseEntity<?> getCoachDetail(@PathVariable String id) {
        CoachResponse coach =  coachService.getDetailById(id);
        if(coach == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin tài khoản!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(coach);
    }
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> editCoach(@RequestBody CoachRequest request, @PathVariable String id) {
        if(coachService.changeDetail(request, id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Thay đổi thông tin thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Thay đổi thông tin thất bại!");
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteId(@PathVariable String id) {
        if(coachService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Xóa tài khoản thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Xóa tài khoản thất bại!");
        }
    }
    @DeleteMapping("/delete-selected")
    public ResponseEntity<?> deleteSelected(@RequestBody DeleteSelectedRequest request) {
        int rows = coachService.deleteByIds(request.getIds());
        if(rows == request.getIds().size()) {
            return ResponseEntity.status(HttpStatus.OK).body("Xóa xe thành công!");
        } else if(rows < request.getIds().size() && rows > 0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Đã xóa "+ rows +" xe thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Xóa xe thất bại!");
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllCoachs(@RequestParam int current, @RequestParam int pageSize) {
        Map<String, Object> daoResponse = coachService.getAllCoachs(current, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(daoResponse);
    }
}
