package com.csdl.tourbusbooking.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.csdl.tourbusbooking.model.TicketRequest;
import com.csdl.tourbusbooking.repository.TicketRepository;

public class TicketRepositoryImpl implements TicketRepository {

	@Override
	public int save(Connection conn, TicketRequest req, long totalPrice) {
	    try {
	        String sql = "INSERT INTO tickets(" +
	                "account_id, depart_trip_id, return_trip_id, ticket_type, total_price, payment_status, created_time" +
	                ") VALUES (?, ?, ?, ?, ?, ?, NOW())";

	        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

	        // account id (FE gửi string, BE convert)
	        Integer accountId = null;
	        try {
	            accountId = Integer.parseInt(req.getAccount_id()); // "acc_002" -> lỗi -> null
	        } catch (Exception ignore) {}

	        if (accountId != null && accountId > 0) {
	            ps.setInt(1, accountId);
	        } else {
	            ps.setNull(1, java.sql.Types.INTEGER);
	        }

	        // depart trip id
	        ps.setInt(2, req.getOutbound().getTrip_id());

	        // return trip id
	        Integer returnId = (req.getReturnTrip() != null &&
	                            req.getReturnTrip().getTrip_id() != null &&
	                            req.getReturnTrip().getTrip_id() > 0)
	                            ? req.getReturnTrip().getTrip_id()
	                            : null;

	        if (returnId != null) {
	            ps.setInt(3, returnId);
	        } else {
	            ps.setNull(3, java.sql.Types.INTEGER);
	        }

	        ps.setString(4, req.getTicket_type());
	        ps.setLong(5, totalPrice);
	        ps.setString(6, "pending");

	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()) return rs.getInt(1);

	        throw new RuntimeException("Không thể lấy ticket_id");

	    } catch (Exception e) {
	        throw new RuntimeException("Lỗi lưu ticket: " + e.getMessage(), e);
	    }
	}

    @Override
    public void updateTotal(Connection conn, int ticketId, long totalPrice) {
        try {
            String sql = "UPDATE tickets SET total_price = ? WHERE ticket_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, totalPrice);
            ps.setInt(2, ticketId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật tổng tiền: " + e.getMessage());
        }
    }
}
