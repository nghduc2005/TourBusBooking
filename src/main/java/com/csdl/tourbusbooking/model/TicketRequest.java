package com.csdl.tourbusbooking.model;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketRequest {

    @JsonProperty("account_id")
    private String account_id;

    private String name;
    private String phone;
    private String address;

    @JsonProperty("ticket_type")
    private String ticket_type;

    @JsonProperty("start_location")
    private String start_location;

    @JsonProperty("end_location")
    private String end_location;

    private String note;

    private Leg outbound;

    // Chấp nhận cả "returnTrip" (mặc định) và "return_trip"
    @JsonAlias("return_trip")
    private Leg returnTrip;

    public static class Leg {

        @JsonProperty("trip_id")
        private Integer trip_id;

        @JsonProperty("coach_id")
        private Integer coach_id;

        // Chấp nhận dạng có/không timezone. Nếu có timezone -> ưu tiên UTC
        @JsonProperty("start_time")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]['Z']", timezone = "UTC")
        private Instant start_time;

        @JsonProperty("coach_type")
        private String coach_type;

        @JsonProperty("ordered_seat")
        private List<String> ordered_seat;

        private Long price;

        // getters/setters ...
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

    // getters/setters ...
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
}
