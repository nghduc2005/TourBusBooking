package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.entity.Return;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returntrip")
public class ReturnTripController {
    @GetMapping
    public List<Return> getAllReturnTrip() {}
    @GetMapping("/{id}")
    public Return getReturnTrips(@PathVariable int id) {}
    @PostMapping
    public String addReturnTrip(@RequestBody Return returnTrip) {}
    @PutMapping("/{id}")
    public String updateReturnTrip(@PathVariable int id, @RequestBody Return returnTrip) {}
}
