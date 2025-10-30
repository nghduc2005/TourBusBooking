package com.csdl.tourbusbooking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookedSeatService {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

}
