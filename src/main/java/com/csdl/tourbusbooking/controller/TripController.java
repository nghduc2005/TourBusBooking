package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.dto.*;
import com.csdl.tourbusbooking.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedules")
public class TripController {
    @Autowired
    private TripService tripService;
    @PostMapping("/create")
    public ResponseEntity<?> createTrip(@RequestBody TripRequest request) {
        if(tripService.create(request)) {
            return ResponseEntity.status(HttpStatus.OK).body("Tạo chuyến đi thành công!");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Tạo chuyến đi thất bại!");
    }
    @GetMapping("/edit/{id}")
    public ResponseEntity<?> getTripDetail(@PathVariable String id) {
        TripResponse trip =  tripService.getDetailById(id);
        if(trip == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin chuyến đi!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(trip);
    }
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable String id, @RequestBody TripRequest request) {
        TripResponse trip = tripService.patchTrip(id, request);
        if(trip != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Thay đổi thông tin thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Thay đổi thông tin thất bại!");
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        if(tripService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Xóa chuyến đi thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Xóa chuyến đi thất bại!");
        }
    }
    @DeleteMapping("/delete-selected")
    public ResponseEntity<?> deleteSelected(@RequestBody DeleteSelectedRequest request) {
        int rows = tripService.deleteByIds(request.getIds());
        if(rows == request.getIds().size()) {
            return ResponseEntity.status(HttpStatus.OK).body("Xóa chuyến đi thành công!");
        } else if(rows < request.getIds().size() && rows > 0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Đã xóa "+ rows +" chuyến đi thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Xóa chuyến đi thất bại!");
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllTrips(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue =
            "5") int pageSize) {
        Map<String, Object> daoResponse = tripService.getAllTrip(current, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(daoResponse);
    }
}
