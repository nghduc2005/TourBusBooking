package com.csdl.tourbusbooking.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import org.springframework.stereotype.Repository;

import com.csdl.tourbusbooking.model.TripSearchRequest;
import com.csdl.tourbusbooking.repository.TripRepository;
import com.csdl.tourbusbooking.repository.entity.TripEntity;
import com.csdl.tourbusbooking.util.DBUtil;

@Repository
public class TripRepositoryImpl implements TripRepository {


    // ==============================================
    // TÌM MỘT CHIỀU
    // ==============================================
    private List<TripEntity> findOneWay(String start, String end, String date) {

        List<TripEntity> result = new ArrayList<>();

        String sql =
            "SELECT t.trip_id, t.start_location, t.end_location, t.start_time, "
          + "t.price, t.status, c.coach_type, c.coach_id, c.total_seat "
          + "FROM trips t "
          + "JOIN coachs c ON t.coach_id = c.coach_id "
          + "WHERE t.start_location LIKE ? "
          + "AND t.end_location LIKE ? "
          + "AND DATE(t.start_time) = ? "
          + "ORDER BY t.start_time ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + start + "%");
            stmt.setString(2, "%" + end + "%");
            stmt.setString(3, date);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TripEntity e = new TripEntity();
                e.setTrip_id(rs.getInt("trip_id"));
                e.setStart_location(rs.getString("start_location"));
                e.setEnd_location(rs.getString("end_location"));
                e.setStart_time(rs.getTimestamp("start_time").toLocalDateTime());
                e.setPrice(rs.getLong("price"));
                e.setStatus(rs.getString("status"));
                e.setCoach_type(rs.getString("coach_type"));
                e.setCoach_id(rs.getInt("coach_id"));
                e.setTotal_seat(rs.getInt("total_seat"));

                // lấy danh sách ghế đã đặt
                e.setOrdered_seat(findBookedSeats(e.getTrip_id()));

                result.add(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }



    // ==============================================
    //  MỘT CHIỀU 
    // ==============================================
    @Override
    public List<TripEntity> findAll(TripSearchRequest req) {
        return findOneWay(
            req.getStart_location(),
            req.getEnd_location(),
            req.getStart_date()
        );
    }


    // ==============================================
    // KHỨ HỒI
    // ==============================================
    @Override
    public Map<String, List<TripEntity>> findRoundTrip(TripSearchRequest req) {

        Map<String, List<TripEntity>> result = new HashMap<>();

        List<TripEntity> depart =
            findOneWay(req.getStart_location(),
                       req.getEnd_location(),
                       req.getStart_date());

        List<TripEntity> ret =
            findOneWay(req.getEnd_location(),
                       req.getStart_location(),
                       req.getEnd_date());

        result.put("depart_trips", depart);
        result.put("return_trips", ret);

        return result;
    }

    // ==============================================
    // GHẾ ĐÃ ĐẶT
    // ==============================================
    @Override
    public List<String> findBookedSeats(Integer tripId) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT seat_label FROM booked_seats WHERE trip_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("seat_label"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Error query booked seats", e);
        }

        return result;
    }
}
