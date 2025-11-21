package com.csdl.tourbusbooking.service;

import com.csdl.tourbusbooking.model.TicketRequest;
import com.csdl.tourbusbooking.model.TicketResponse;

public interface TicketInterface {
    TicketResponse create(TicketRequest req);
}
