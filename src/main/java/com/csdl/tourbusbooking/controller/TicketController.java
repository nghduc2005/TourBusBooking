package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.dto.CoachResponse;
import com.csdl.tourbusbooking.dto.TicketResponse;
import com.csdl.tourbusbooking.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking-history")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @GetMapping
    public ResponseEntity<?> getAllTicket(@RequestParam int current,
                                                             @RequestParam int pageSize,  HttpSession session) {
        String username = (String) session.getAttribute("account");
        String role = (String) session.getAttribute("role");
        Map<String, Object> daoResponse = ticketService.getAllTickets(current, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(daoResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllTickets(@RequestParam int current,
                                                              @RequestParam int pageSize, @PathVariable String id) {
        Map<String, Object> daoResponse = ticketService.getTicketsById(id, current, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(daoResponse);
    }
}
