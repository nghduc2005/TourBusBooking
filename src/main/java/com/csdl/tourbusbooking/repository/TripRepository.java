package com.csdl.tourbusbooking.repository;

import java.util.List;
import java.util.Map;

import com.csdl.tourbusbooking.model.TripSearchRequest;
import com.csdl.tourbusbooking.repository.entity.TripEntity;

public interface TripRepository {
    List<TripEntity> findAll(TripSearchRequest request);
    List<String> findBookedSeats(Integer tripId);
    Map<String, List<TripEntity>> findRoundTrip(TripSearchRequest req);


}
