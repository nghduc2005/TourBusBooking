package com.csdl.tourbusbooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class TripController {
    @GetMapping("api/trip")
    public String Trip() {
        return "Hello World";
    }
}