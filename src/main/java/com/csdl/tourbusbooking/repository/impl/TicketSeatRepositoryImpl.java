package com.csdl.tourbusbooking.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.csdl.tourbusbooking.repository.TicketSeatRepository;

public class TicketSeatRepositoryImpl implements TicketSeatRepository {

    @Override
    public void save(Connection conn, int ticketId, int tripId, String seatLabel) {
        try {
            String sql = "INSERT INTO booked_seats (ticket_id, trip_id, seat_label) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ticketId);
            ps.setInt(2, tripId);
            ps.setString(3, seatLabel);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi lưu ghế " + seatLabel + ": " + e.getMessage());
        }
    }

    @Override
    public boolean seatBooked(Connection conn, int tripId, String seatLabel) {
        try {
            String sql = "SELECT COUNT(*) FROM booked_seats WHERE trip_id=? AND seat_label=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, tripId);
            ps.setString(2, seatLabel);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi kiểm tra ghế: " + e.getMessage());
        }
    }
}
