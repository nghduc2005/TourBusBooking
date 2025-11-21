package com.csdl.tourbusbooking.repository;

import java.sql.Connection;

public interface TicketSeatRepository {

    void save(Connection conn, int ticketId, int tripId, String seatLabel);

    boolean seatBooked(Connection conn, int tripId, String seatLabel);
}
