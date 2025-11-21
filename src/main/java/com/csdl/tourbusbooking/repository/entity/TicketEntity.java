package com.csdl.tourbusbooking.repository.entity;

import java.time.Instant;

public class TicketEntity {

    private Integer ticketId;
    private Integer accountId;
    private Integer departTripId;
    private Integer returnTripId;
    private String ticketType;
    private Long totalPrice;
    private String paymentStatus;
    private Instant createdTime;
    private String note;

    // ====== GETTER / SETTER ======

    public Integer getTicketId() { return ticketId; }
    public void setTicketId(Integer ticketId) { this.ticketId = ticketId; }

    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }

    public Integer getDepartTripId() { return departTripId; }
    public void setDepartTripId(Integer departTripId) { this.departTripId = departTripId; }

    public Integer getReturnTripId() { return returnTripId; }
    public void setReturnTripId(Integer returnTripId) { this.returnTripId = returnTripId; }

    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }

    public Long getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Long totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Instant getCreatedTime() { return createdTime; }
    public void setCreatedTime(Instant createdTime) { this.createdTime = createdTime; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
