package com.csdl.tourbusbooking.api;

import org.springframework.web.bind.annotation.*;

import com.csdl.tourbusbooking.model.TicketRequest;
import com.csdl.tourbusbooking.model.TicketResponse;
import com.csdl.tourbusbooking.service.impl.TicketServiceImpl;
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class TicketAPI {

    private TicketServiceImpl service = new TicketServiceImpl();

    @PostMapping("/tickets")
    public TicketResponse createTicket(@RequestBody TicketRequest req) {
        return service.create(req);
    }
}
