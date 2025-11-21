package com.csdl.tourbusbooking.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;

import com.csdl.tourbusbooking.dto.TicketRequest.Leg;

import java.time.Instant;
@Data
public class TripResponse {
    private int trip_id;
    private String start_location;
    private String end_location;
    private Instant start_time;
    private int price;
    private String status;
    private String coach_type;
    private int coach_id;
    private int total_seat;
    private String[] ordered_seats;
    private Integer ticket_id;
    private String account_id;
    private String name;
    private String phone;
    private String address;
    private String ticket_type;
    private String payment_status;
    private Instant created_time;
    private String note;
    private Long total_price;

    private Leg outbound;
    private Leg returnTrip; // đổi tên
        public Integer getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(Integer ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public Instant getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Instant created_time) {
        this.created_time = created_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getEnd_location() {
        return end_location;
    }

    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    public Long getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Long total_price) {
        this.total_price = total_price;
    }

    public Leg getOutbound() {
        return outbound;
    }

    public void setOutbound(Leg outbound) {
        this.outbound = outbound;
    }

    public Leg getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(Leg returnTrip) {
        this.returnTrip = returnTrip;
    }

    // Inner class
    public static class Leg {
        private Integer trip_id;
        private Integer coach_id;
        private Instant start_time;
        private String coach_type;
        private List<String> ordered_seat;
        private Long price;

        public Integer getTrip_id() {
            return trip_id;
        }

        public void setTrip_id(Integer trip_id) {
            this.trip_id = trip_id;
        }

        public Integer getCoach_id() {
            return coach_id;
        }

        public void setCoach_id(Integer coach_id) {
            this.coach_id = coach_id;
        }

        public Instant getStart_time() {
            return start_time;
        }

        public void setStart_time(Instant start_time) {
            this.start_time = start_time;
        }

        public String getCoach_type() {
            return coach_type;
        }

        public void setCoach_type(String coach_type) {
            this.coach_type = coach_type;
        }

        public List<String> getOrdered_seat() {
            return ordered_seat;
        }

        public void setOrdered_seat(List<String> ordered_seat) {
            this.ordered_seat = ordered_seat;
        }

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }
    }
}
