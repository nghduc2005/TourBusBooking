package com.csdl.tourbusbooking.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csdl.tourbusbooking.model.TripDTO;
import com.csdl.tourbusbooking.model.TripSearchRequest;
import com.csdl.tourbusbooking.repository.TripRepository;
import com.csdl.tourbusbooking.repository.entity.TripEntity;
import com.csdl.tourbusbooking.service.TripInterface;

@Service
public class TripServiceImpl implements TripInterface {

    @Autowired
    private TripRepository tripRepository;

    // Tìm chuyến 1 chiều (CÓ ordered_seat)
    @Override
    public List<TripDTO> findAll(TripSearchRequest request) {
        List<TripEntity> trips = tripRepository.findAll(request);
        List<TripDTO> result = new ArrayList<>();

        for (TripEntity t : trips) {
            TripDTO dto = new TripDTO();
            dto.setTrip_id(t.getTrip_id());
            dto.setStart_location(t.getStart_location());
            dto.setEnd_location(t.getEnd_location());
            dto.setStart_time(t.getStart_time());
            dto.setPrice(t.getPrice());
            dto.setStatus(t.getStatus());
            dto.setCoach_type(t.getCoach_type());
            dto.setCoach_id(t.getCoach_id());
            dto.setTotal_seat(t.getTotal_seat());

            // Lấy ghế đã đặt
            dto.setOrdered_seat(tripRepository.findBookedSeats(t.getTrip_id()));

            result.add(dto);
        }

        return result;
    }
    //  Tìm chuyến khứ hồi (CÓ ordered_seat cho cả đi + về)
    @Override
    public Map<String, List<TripDTO>> findRoundTrip(TripSearchRequest req) {

        // Chuyến đi
        List<TripDTO> departTrips = findAll(req);

        // Tạo request chuyến về (đổi chiều, đổi ngày)
        TripSearchRequest returnReq = new TripSearchRequest();
        returnReq.setStart_location(req.getEnd_location());
        returnReq.setEnd_location(req.getStart_location());
        returnReq.setStart_date(req.getEnd_date()); 
        returnReq.setEnd_date(null); 
        returnReq.setPage(req.getPage());
        returnReq.setSize(req.getSize());

      
        List<TripDTO> returnTrips = findAll(returnReq);

        
        return Map.of(
                "depart_trips", departTrips,
                "return_trips", returnTrips
        );
    }
}
