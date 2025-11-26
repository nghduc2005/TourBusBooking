package com.csdl.tourbusbooking.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.csdl.tourbusbooking.model.TripDTO;
import com.csdl.tourbusbooking.model.TripSearchRequest;
import com.csdl.tourbusbooking.service.TripInterface;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class TripAPI {

    @Autowired
    private TripInterface tripService;

    // API tìm chuyến (1 chiều + khứ hồi)
    @GetMapping(value = "/schedules/search", params = { "start_location", "end_location", "start_date" })
    public ResponseEntity<?> schedulesSearch(TripSearchRequest request) {
        boolean isRoundTrip = "true".equalsIgnoreCase(request.getRound_trip()) ||
                (request.getStart_date() != null && request.getEnd_date() != null);

        Map<String, Object> result = new LinkedHashMap<>();

        if (isRoundTrip) {
            Map<String, List<TripDTO>> data = tripService.findRoundTrip(request);

            List<TripDTO> depart = data.get("depart_trips");
            List<TripDTO> back = data.get("return_trips");

            result.put("outbound", depart != null ? depart : List.of());
            result.put("returnTrip", back != null ? back : List.of());

            return ResponseEntity.ok(result);
        }

        // Một chiều
        List<TripDTO> list = tripService.findAll(request);

        result.put("outbound", list != null ? list : List.of());
        result.put("returnTrip", List.of()); // luôn trả mảng rỗng

        return ResponseEntity.ok(result);
    }

    //
    // // Chi tiết 1 chuyến
    //
    // @GetMapping("/{id}")
    // public ResponseEntity<?> getTripDetail(@PathVariable Integer id) {
    // TripDTO t = tripService.findById(id);
    // if (t == null) return ResponseEntity.notFound().build();
    // return ResponseEntity.ok(Map.of("data", t));
    // }
}