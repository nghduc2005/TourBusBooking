package com.csdl.tourbusbooking.service;

import com.csdl.tourbusbooking.dto.TicketResponse;
import com.csdl.tourbusbooking.model.OutBound;
import com.csdl.tourbusbooking.model.ReturnTrip;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
@RequiredArgsConstructor
public class TicketService {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public Map<String, Object> getAllTickets(int current, int pageSize) {
        String sql ="SELECT  tk.ticket_id,a.account_id,a.name,a.address,a.phone,tk.ticket_type,tk.total_price,tk" +
                ".payment_status,tk.created_time,a.note,tc1.trip_id AS depart_trip_id,tc1.start_location AS " +
                "depart_start_location,tc1.end_location AS depart_end_location,tc1.start_time AS depart_start_time," +
                "tc1.coach_id AS depart_coach_id,tc1.coach_name AS depart_coach_name,tc1.coach_type AS " +
                "depart_coach_type,tc1.price AS depart_price,os1.ordered_seats AS depart_ordered_seats,tc2.trip_id AS" +
                " return_trip_id,tc2.start_time AS return_start_time,tc2.coach_id AS return_coach_id,tc2.coach_name " +
                "AS return_coach_name,tc2.coach_type AS return_coach_type,tc2.price AS return_price,os2.ordered_seats" +
                " AS return_ordered_seats FROM tickets tk JOIN accounts a ON tk.account_id = a.account_id JOIN " +
                "(SELECT t.trip_id, t.start_location, t.end_location, t.start_time, t.price,c.coach_id, c.coach_name," +
                " c.coach_type, c.total_seat, c.status AS coach_status FROM trips t JOIN coachs c ON t.coach_id = c" +
                ".coach_id) tc1 ON tc1.trip_id = tk.depart_trip_id JOIN (SELECT ticket_id, trip_id, GROUP_CONCAT" +
                "(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS ordered_seats FROM booked_seats GROUP" +
                " BY ticket_id, trip_id) os1 ON tk.ticket_id = os1.ticket_id AND os1.trip_id = tk.depart_trip_id LEFT" +
                " JOIN (SELECT t.trip_id, t.start_time, t.price, c.coach_id, c.coach_name, c.coach_type FROM trips t " +
                "JOIN coachs c ON t.coach_id = c.coach_id) tc2 ON tc2.trip_id = tk.return_trip_id LEFT JOIN (SELECT " +
                "ticket_id,trip_id, GROUP_CONCAT(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS " +
                "ordered_seats FROM booked_seats GROUP BY ticket_id, trip_id) os2 ON tk.ticket_id = os2.ticket_id AND" +
                " os2.trip_id = tk.return_trip_id order by created_time limit ? offset ?; ";

        String countSQL = "SELECT  COUNT(DISTINCT tk.ticket_id) AS total FROM tickets tk JOIN accounts a ON tk" +
                ".account_id = " +
                "a" +
                ".account_id JOIN " +
                "(SELECT t.trip_id, t.start_location, t.end_location, t.start_time, t.price,c.coach_id, c.coach_name," +
                " c.coach_type, c.total_seat, c.status AS coach_status FROM trips t JOIN coachs c ON t.coach_id = c" +
                ".coach_id) tc1 ON tc1.trip_id = tk.depart_trip_id JOIN (SELECT ticket_id, trip_id, GROUP_CONCAT" +
                "(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS ordered_seats FROM booked_seats GROUP" +
                " BY ticket_id, trip_id) os1 ON tk.ticket_id = os1.ticket_id AND os1.trip_id = tk.depart_trip_id LEFT" +
                " JOIN (SELECT t.trip_id, t.start_time, t.price, c.coach_id, c.coach_name, c.coach_type FROM trips t " +
                "JOIN coachs c ON t.coach_id = c.coach_id) tc2 ON tc2.trip_id = tk.return_trip_id LEFT JOIN (SELECT " +
                "ticket_id,trip_id, GROUP_CONCAT(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS " +
                "ordered_seats FROM booked_seats GROUP BY ticket_id, trip_id) os2 ON tk.ticket_id = os2.ticket_id AND" +
                " os2.trip_id = tk.return_trip_id";
        try {
            List<TicketResponse> ticketList = jdbcTemplate.query(
                    sql,
                    new Object[]{pageSize, (current-1)*pageSize},
                    (rs, rowNum) -> {
                        TicketResponse ticket = new TicketResponse();

                        // Thông tin chính
                        ticket.setTicket_id(rs.getInt("ticket_id"));
                        ticket.setAccount_id(rs.getInt("account_id"));
                        ticket.setName(rs.getString("name"));
                        ticket.setPhone(rs.getString("phone"));
                        ticket.setTicket_type(rs.getString("ticket_type"));
                        ticket.setTotal_price(rs.getInt("total_price"));
                        ticket.setPayment_status(rs.getString("payment_status"));
                        ticket.setCreated_time(rs.getTimestamp("created_time").toInstant());
                        ticket.setNote(rs.getString("note"));
                        ticket.setStart_location(rs.getString("depart_start_location"));
                        ticket.setEnd_location(rs.getString("depart_end_location"));

                        // --- Chuyến đi ---
                        OutBound outBound = new OutBound();
                        outBound.setTrip_id(rs.getInt("depart_trip_id"));
                        outBound.setStart_time(rs.getTimestamp("depart_start_time").toInstant());
                        outBound.setCoach_type(rs.getString("depart_coach_type"));
                        outBound.setCoach_id(rs.getInt("depart_coach_id"));
                        outBound.setPrice(rs.getInt("depart_price"));

                        String departSeats = rs.getString("depart_ordered_seats");
                        outBound.setOrdered_seat(departSeats != null ?
                                Arrays.asList(departSeats.split(",")) : new ArrayList<>());

                        // --- Chuyến về (nếu có) ---
                        ReturnTrip returnTrip = new ReturnTrip();
                        if (rs.getObject("return_trip_id") != null) {
                            returnTrip.setTrip_id(rs.getInt("return_trip_id"));
                            Timestamp returnTime = rs.getTimestamp("return_start_time");
                            returnTrip.setStart_time(returnTime != null ? returnTime.toInstant() : null);
                            returnTrip.setCoach_type(rs.getString("return_coach_type"));
                            returnTrip.setCoach_id(rs.getInt("return_coach_id"));
                            returnTrip.setPrice(rs.getInt("return_price"));

                            String returnSeats = rs.getString("return_ordered_seats");
                            returnTrip.setOrdered_seat(returnSeats != null ?
                                    Arrays.asList(returnSeats.split(",")) : new ArrayList<>());
                        } else {
                            returnTrip.setOrdered_seat(new ArrayList<>());
                        }

                        // --- Gán lại ---
                        ticket.setOutbound(outBound);
                        if(ticket.getTicket_type().equals("oneway")){
                            ticket.setRound(null);
                        } else {
                            ticket.setRound(returnTrip);
                        }
                        return ticket;
                    }
            );

            int total = jdbcTemplate.queryForObject(countSQL, Integer.class);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("tickets", ticketList);
            return map;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL: " + e.getMessage());
            return null;
        } catch (DataAccessException e) {
            System.out.println("Lỗi truy cập CSDL: " + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getTicketsById(String id, int current, int pageSize) {
        String sql ="SELECT  tk.ticket_id,a.account_id,a.name,a.address,a.phone,tk.ticket_type,tk.total_price,tk" +
                ".payment_status,tk.created_time,a.note,tc1.trip_id AS depart_trip_id,tc1.start_location AS " +
                "depart_start_location,tc1.end_location AS depart_end_location,tc1.start_time AS depart_start_time," +
                "tc1.coach_id AS depart_coach_id,tc1.coach_name AS depart_coach_name,tc1.coach_type AS " +
                "depart_coach_type,tc1.price AS depart_price,os1.ordered_seats AS depart_ordered_seats,tc2.trip_id AS" +
                " return_trip_id,tc2.start_time AS return_start_time,tc2.coach_id AS return_coach_id,tc2.coach_name " +
                "AS return_coach_name,tc2.coach_type AS return_coach_type,tc2.price AS return_price,os2.ordered_seats" +
                " AS return_ordered_seats FROM tickets tk JOIN accounts a ON tk.account_id = a.account_id JOIN " +
                "(SELECT t.trip_id, t.start_location, t.end_location, t.start_time, t.price,c.coach_id, c.coach_name," +
                " c.coach_type, c.total_seat, c.status AS coach_status FROM trips t JOIN coachs c ON t.coach_id = c" +
                ".coach_id) tc1 ON tc1.trip_id = tk.depart_trip_id JOIN (SELECT ticket_id, trip_id, GROUP_CONCAT" +
                "(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS ordered_seats FROM booked_seats GROUP" +
                " BY ticket_id, trip_id) os1 ON tk.ticket_id = os1.ticket_id AND os1.trip_id = tk.depart_trip_id LEFT" +
                " JOIN (SELECT t.trip_id, t.start_time, t.price, c.coach_id, c.coach_name, c.coach_type FROM trips t " +
                "JOIN coachs c ON t.coach_id = c.coach_id) tc2 ON tc2.trip_id = tk.return_trip_id LEFT JOIN (SELECT " +
                "ticket_id,trip_id, GROUP_CONCAT(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS " +
                "ordered_seats FROM booked_seats GROUP BY ticket_id, trip_id) os2 ON tk.ticket_id = os2.ticket_id AND" +
                " os2.trip_id = tk.return_trip_id where a.account_id = ? order by created_time limit ? offset ?";
        String countSQL = "SELECT COUNT(DISTINCT tk.ticket_id) AS total FROM tickets tk JOIN accounts a ON " +
                "tk.account_id = a.account_id JOIN " +
                "(SELECT t.trip_id, t.start_location, t.end_location, t.start_time, t.price,c.coach_id, c.coach_name," +
                " c.coach_type, c.total_seat, c.status AS coach_status FROM trips t JOIN coachs c ON t.coach_id = c" +
                ".coach_id) tc1 ON tc1.trip_id = tk.depart_trip_id JOIN (SELECT ticket_id, trip_id, GROUP_CONCAT" +
                "(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS ordered_seats FROM booked_seats GROUP" +
                " BY ticket_id, trip_id) os1 ON tk.ticket_id = os1.ticket_id AND os1.trip_id = tk.depart_trip_id LEFT" +
                " JOIN (SELECT t.trip_id, t.start_time, t.price, c.coach_id, c.coach_name, c.coach_type FROM trips t " +
                "JOIN coachs c ON t.coach_id = c.coach_id) tc2 ON tc2.trip_id = tk.return_trip_id LEFT JOIN (SELECT " +
                "ticket_id,trip_id, GROUP_CONCAT(DISTINCT seat_label ORDER BY seat_label ASC SEPARATOR ',') AS " +
                "ordered_seats FROM booked_seats GROUP BY ticket_id, trip_id) os2 ON tk.ticket_id = os2.ticket_id AND" +
                " os2.trip_id = tk.return_trip_id where a.account_id = ?";
        try {
            List<TicketResponse> ticketList = jdbcTemplate.query(
                    sql,
                    new Object[]{id, pageSize, (current-1)*pageSize},
                    (rs, rowNum) -> {
                        TicketResponse ticket = new TicketResponse();

                        // Thông tin chính
                        ticket.setTicket_id(rs.getInt("ticket_id"));
                        ticket.setAccount_id(rs.getInt("account_id"));
                        ticket.setName(rs.getString("name"));
                        ticket.setPhone(rs.getString("phone"));
                        ticket.setTicket_type(rs.getString("ticket_type"));
                        ticket.setTotal_price(rs.getInt("total_price"));
                        ticket.setPayment_status(rs.getString("payment_status"));
                        ticket.setCreated_time(rs.getTimestamp("created_time").toInstant());
                        ticket.setNote(rs.getString("note"));
                        ticket.setStart_location(rs.getString("depart_start_location"));
                        ticket.setEnd_location(rs.getString("depart_end_location"));

                        // --- Chuyến đi ---
                        OutBound outBound = new OutBound();
                        outBound.setTrip_id(rs.getInt("depart_trip_id"));
                        outBound.setStart_time(rs.getTimestamp("depart_start_time").toInstant());
                        outBound.setCoach_type(rs.getString("depart_coach_type"));
                        outBound.setCoach_id(rs.getInt("depart_coach_id"));
                        outBound.setPrice(rs.getInt("depart_price"));

                        String departSeats = rs.getString("depart_ordered_seats");
                        outBound.setOrdered_seat(departSeats != null ?
                                Arrays.asList(departSeats.split(",")) : new ArrayList<>());

                        // --- Chuyến về (nếu có) ---
                        ReturnTrip returnTrip = new ReturnTrip();
                        if (rs.getObject("return_trip_id") != null) {
                            returnTrip.setTrip_id(rs.getInt("return_trip_id"));
                            Timestamp returnTime = rs.getTimestamp("return_start_time");
                            returnTrip.setStart_time(returnTime != null ? returnTime.toInstant() : null);
                            returnTrip.setCoach_type(rs.getString("return_coach_type"));
                            returnTrip.setCoach_id(rs.getInt("return_coach_id"));
                            returnTrip.setPrice(rs.getInt("return_price"));

                            String returnSeats = rs.getString("return_ordered_seats");
                            returnTrip.setOrdered_seat(returnSeats != null ?
                                    Arrays.asList(returnSeats.split(",")) : new ArrayList<>());
                        } else {
                            returnTrip.setOrdered_seat(new ArrayList<>());
                        }

                        // --- Gán lại ---
                        ticket.setOutbound(outBound);
                        if(ticket.getTicket_type().equals("oneway")){
                            ticket.setRound(null);
                        } else {
                            ticket.setRound(returnTrip);
                        }
                        return ticket;
                    }
            );
            int total = jdbcTemplate.queryForObject(countSQL, Integer.class, id);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("tickets", ticketList);
            return map;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL: " + e.getMessage());
            return null;
        } catch (DataAccessException e) {
            System.out.println("Lỗi truy cập CSDL: " + e.getMessage());
            return null;
        }
    }
}
