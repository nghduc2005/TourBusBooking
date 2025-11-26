package com.csdl.tourbusbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

public class TripSearchRequest {

    private String start_location;
    private String end_location;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                timezone = "UTC")
    private Instant start_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                timezone = "UTC")
    private Instant end_date;

    private String round_trip; // "true", "false", hoặc null
    private int page = 1;
    private int size = 10;

    // GETTER/SETTER
    public String getStart_location() { return start_location; }
    public void setStart_location(String start_location) { this.start_location = start_location; }

    public String getEnd_location() { return end_location; }
    public void setEnd_location(String end_location) { this.end_location = end_location; }

    public Instant getStart_date() { return start_date; }
    public void setStart_date(Instant start_date) { this.start_date = start_date; }

    public Instant getEnd_date() { return end_date; }
    public void setEnd_date(Instant end_date) { this.end_date = end_date; }

    public String getRound_trip() { return round_trip; }
    public void setRound_trip(String round_trip) { this.round_trip = round_trip; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}