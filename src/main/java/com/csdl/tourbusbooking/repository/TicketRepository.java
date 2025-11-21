package com.csdl.tourbusbooking.repository;

import java.sql.Connection;
import com.csdl.tourbusbooking.model.TicketRequest;

public interface TicketRepository {

    int save(Connection conn, TicketRequest req, long totalPrice);

    void updateTotal(Connection conn, int ticketId, long totalPrice);
}
