package com.csdl.tourbusbooking.controller;
import com.csdl.tourbusbooking.dto.CoachResponse;
import com.csdl.tourbusbooking.dto.TicketResponse;
import com.csdl.tourbusbooking.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking-history")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTicket(HttpSession session) {
        String username = (String) session.getAttribute("account");
        List<TicketResponse> orderedTicketList = ticketService.getAllTickets(username);
        return ResponseEntity.status(HttpStatus.OK).body(orderedTicketList);
    }
}
