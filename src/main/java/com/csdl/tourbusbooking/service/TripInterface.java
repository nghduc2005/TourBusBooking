package com.csdl.tourbusbooking.service;

import java.util.List;
import java.util.Map;

import com.csdl.tourbusbooking.model.TripDTO;
import com.csdl.tourbusbooking.model.TripSearchRequest;


import com.csdl.tourbusbooking.dto.TicketRequest;
import com.csdl.tourbusbooking.dto.TicketResponse;

public interface TripInterface {

    // Tìm kiếm danh sách chuyến
    List<TripDTO> findAll(TripSearchRequest request);
    Map<String, List<TripDTO>> findRoundTrip(TripSearchRequest request);
}
