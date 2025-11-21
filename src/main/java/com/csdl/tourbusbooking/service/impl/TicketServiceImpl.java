package com.csdl.tourbusbooking.service.impl;

import java.sql.Connection;
import java.time.Instant;

import com.csdl.tourbusbooking.model.TicketRequest;
import com.csdl.tourbusbooking.model.TicketResponse;
import com.csdl.tourbusbooking.repository.TicketRepository;
import com.csdl.tourbusbooking.repository.TicketSeatRepository;
import com.csdl.tourbusbooking.repository.impl.TicketRepositoryImpl;
import com.csdl.tourbusbooking.repository.impl.TicketSeatRepositoryImpl;
import com.csdl.tourbusbooking.service.TicketInterface;
import com.csdl.tourbusbooking.util.DBUtil;

public class TicketServiceImpl implements TicketInterface {

    private final TicketRepository ticketRepo = new TicketRepositoryImpl();
    private final TicketSeatRepository ticketSeatRepo = new TicketSeatRepositoryImpl();

    @Override
    public TicketResponse create(TicketRequest req) {

        // Chuẩn hóa account_id
        Integer accountId = extractId(req.getAccount_id());
        if (accountId == null) {
            throw new RuntimeException("Account ID không hợp lệ!");
        }
        req.setAccount_id(String.valueOf(accountId));

        // Validate lượt đi
        if (req.getOutbound() == null ||
            req.getOutbound().getOrdered_seat() == null ||
            req.getOutbound().getOrdered_seat().isEmpty()) {
            throw new RuntimeException("Bạn chưa chọn ghế cho lượt đi!");
        }

        // Validate lượt về nếu là vé khứ hồi
        boolean isReturn =
        	    req.getTicket_type() != null &&
        	    req.getTicket_type().equalsIgnoreCase("returnTrip");

        if (isReturn) {
            if (req.getReturnTrip() == null ||
                req.getReturnTrip().getOrdered_seat() == null ||
                req.getReturnTrip().getOrdered_seat().isEmpty()) {
                throw new RuntimeException("Bạn chưa chọn ghế cho lượt về!");
            }
        }

        // Transaction với try-with-resources
        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            // Kiểm tra ghế lượt đi
            for (String seat : req.getOutbound().getOrdered_seat()) {
                if (ticketSeatRepo.seatBooked(conn, req.getOutbound().getTrip_id(), seat)) {
                    throw new RuntimeException("Ghế " + seat + " lượt đi đã được đặt!");
                }
            }

            // Kiểm tra ghế lượt về
            if (isReturn) {
                for (String seat : req.getReturnTrip().getOrdered_seat()) {
                    if (ticketSeatRepo.seatBooked(conn, req.getReturnTrip().getTrip_id(), seat)) {
                        throw new RuntimeException("Ghế " + seat + " lượt về đã được đặt!");
                    }
                }
            }

            // Tính tiền
            long total = req.getOutbound().getPrice()
                        * req.getOutbound().getOrdered_seat().size();

            if (isReturn) {
                total += req.getReturnTrip().getPrice()
                        * req.getReturnTrip().getOrdered_seat().size();
            }

            // Lưu ticket
            int ticketId = ticketRepo.save(conn, req, total);

            // Lưu ghế lượt đi
            for (String seat : req.getOutbound().getOrdered_seat()) {
                ticketSeatRepo.save(conn, ticketId, req.getOutbound().getTrip_id(), seat);
            }

            // Lưu ghế lượt về
            if (isReturn) {
                for (String seat : req.getReturnTrip().getOrdered_seat()) {
                    ticketSeatRepo.save(conn, ticketId, req.getReturnTrip().getTrip_id(), seat);
                }
            }

            conn.commit();

            // Build response
            TicketResponse res = new TicketResponse();
            res.setTicket_id(ticketId);
            res.setAccount_id(req.getAccount_id());
            res.setName(req.getName());
            res.setPhone(req.getPhone());
            res.setAddress(req.getAddress());
            res.setTicket_type(req.getTicket_type());
            res.setPayment_status("pending");
            res.setCreated_time(Instant.now());
            res.setNote(req.getNote());
            res.setStart_location(req.getStart_location());
            res.setEnd_location(req.getEnd_location());
            res.setTotal_price(total);

            // outbound leg
            TicketResponse.Leg out = new TicketResponse.Leg();
            out.setTrip_id(req.getOutbound().getTrip_id());
            out.setCoach_id(req.getOutbound().getCoach_id());
            out.setStart_time(req.getOutbound().getStart_time());
            out.setCoach_type(req.getOutbound().getCoach_type());
            out.setOrdered_seat(req.getOutbound().getOrdered_seat());
            out.setPrice(req.getOutbound().getPrice());
            res.setOutbound(out);

            // return leg
            if (isReturn) {
                TicketResponse.Leg ret = new TicketResponse.Leg();
                ret.setTrip_id(req.getReturnTrip().getTrip_id());
                ret.setCoach_id(req.getReturnTrip().getCoach_id());
                ret.setStart_time(req.getReturnTrip().getStart_time());
                ret.setCoach_type(req.getReturnTrip().getCoach_type());
                ret.setOrdered_seat(req.getReturnTrip().getOrdered_seat());
                ret.setPrice(req.getReturnTrip().getPrice());
                res.setReturnTrip(ret);
            }

            return res;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo vé: " + e.getMessage(), e);
        }
    }

    private Integer extractId(String raw) {
        if (raw == null) return null;
        String digits = raw.replaceAll("\\D+", "");
        if (digits.isEmpty()) return null;
        return Integer.parseInt(digits);
    }
}
