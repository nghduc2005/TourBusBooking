package com.csdl.tourbusbooking.model;

import java.time.LocalDateTime;
import java.util.List;

public class TripDTO {

    private Integer trip_id;
    private String start_location;
    private String end_location;
    private LocalDateTime start_time;
    private Long price;
    private String status;

    private Integer coach_id;
    private String coach_type;
    private Integer total_seat;

    private List<String> ordered_seat; // Danh sách ghế đã đặt

    // GETTER & SETTER
    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
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

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(Integer coach_id) {
        this.coach_id = coach_id;
    }

    public String getCoach_type() {
        return coach_type;
    }

    public void setCoach_type(String coach_type) {
        this.coach_type = coach_type;
    }

    public Integer getTotal_seat() {
        return total_seat;
    }

    public void setTotal_seat(Integer total_seat) {
        this.total_seat = total_seat;
    }

    public List<String> getOrdered_seat() {
        return ordered_seat;
    }

    public void setOrdered_seat(List<String> ordered_seat) {
        this.ordered_seat = (ordered_seat == null) ? List.of() : ordered_seat;
    }

}
