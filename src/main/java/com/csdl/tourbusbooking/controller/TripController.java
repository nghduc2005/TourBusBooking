package com.csdl.tourbusbooking.controller;

import com.csdl.tourbusbooking.entity.Trip;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class TripController {
    @GetMapping
    public List<Trip> getAllTrip() {}
    @GetMapping("/{id}")
    public Trip getTrips(@PathVariable int id) {}
    @PostMapping
    public String addTrip(@RequestBody Trip trip) {}
    @PutMapping("/{id}")
    public String updateTrip(@PathVariable int id, @RequestBody Trip trip) {}
}