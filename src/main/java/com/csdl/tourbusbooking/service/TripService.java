package com.csdl.tourbusbooking.service;

import com.csdl.tourbusbooking.dto.CoachResponse;
import com.csdl.tourbusbooking.dto.TripRequest;
import com.csdl.tourbusbooking.dto.TripResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    public Boolean create(TripRequest request) {
        String sql = "insert into trips(start_location, end_location, start_time, price, coach_id) values(?, ?, ?, ?," +
                " ?)";
        try{
            jdbcTemplate.update(
                    sql,
                    request.getStart_location(),
                    request.getEnd_location(),
                    request.getStart_time(),
                    request.getPrice(),
                    request.getCoach_id()
            );
            return true;
        } catch (DataIntegrityViolationException e) {
            //trùng khóa chính/vi phạm khóa ngoại/null tại not null/vượt kích thước định nghĩa
            System.out.println(e.getMessage());
            System.out.println("Lỗi ràng buộc dữ liệu");
            return false;
        } catch (BadSqlGrammarException e) {
            System.out.println(e.getMessage());
            System.out.println("Sai cú pháp SQL");
            return false;
        }
    }
    public TripResponse getDetailById(String id) {
        String sql = "select start_location, end_location, start_time, price, coach_id from trips where trip_id= ?";
        try{
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    new RowMapper<TripResponse>() {
                        public TripResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                            TripResponse trip = new TripResponse();
                            Timestamp timestamp = rs.getTimestamp("start_time");
                            trip.setStart_location(rs.getString("start_location"));
                            trip.setEnd_location(rs.getString("end_location"));
                            trip.setStart_time(timestamp.toInstant());
                            trip.setPrice(rs.getInt("price"));
                            trip.setCoach_id(rs.getInt("coach_id"));
                            return trip;
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null; //không tìm thấy
        }
    }
    public Boolean deleteById(String id) {
        String sql =  "delete from trips where trip_id = ?";
        try{
            int rows = jdbcTemplate.update(
                    sql,
                    id
            );
            if(rows == 1) {
                return true;
            }
            return false;
        } catch (DataIntegrityViolationException e) {
            //trùng khóa chính/vi phạm khóa ngoại/null tại not null/vượt kích thước định nghĩa
            System.out.println(e.getMessage());
            System.out.println("Lỗi ràng buộc dữ liệu");
            return false;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL");
            return false;
        }
    }
    public int deleteByIds (List<Long> ids) {
        String sql = "DELETE FROM trips WHERE trip_id IN (" +
                //Đổi mỗi phần tử thuộc list thành ?
                ids
                        .stream().map(id -> "?") //tự động duyệt tuần tự, thay các phần tử thành ?
                        .collect(Collectors.joining(",")) + ")"; //nhặt các kết quả và join để tạo dạng (?, ? ,...,?)
        try {
            return jdbcTemplate.update(sql, ids.toArray());
        } catch (DataIntegrityViolationException e) {
            //trùng khóa chính/vi phạm khóa ngoại/null tại not null/vượt kích thước định nghĩa
            System.out.println(e.getMessage());
            System.out.println("Lỗi ràng buộc dữ liệu");
            return 0;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL");
            return 0;
        }
    }
    public Map<String, Object> getAllTrip(int current, int pageSize) {
        String sql = "SELECT t.trip_id, t.start_location, t.end_location, t.start_time, t.price, t.status, t" +
                ".coach_id, c.coach_type, c.total_seat FROM trips t JOIN coachs c ON t.coach_id = c.coach_id order by" +
                " trip_id" +
                " " +
                "limit " +
                "?" +
                " offset ?";
        String countSQL = "SELECT count(distinct t.trip_id, t.start_location, t.end_location, t.start_time, t.price, " +
                "t" +
                ".status, t" +
                ".coach_id, c.coach_type, c.total_seat) total FROM trips t JOIN coachs c ON t.coach_id = c.coach_id";
        try{
            List<TripResponse> tripsList = jdbcTemplate.query(
                    sql,
                    new Object[]{pageSize, (current-1)*pageSize},
                    (rs, rowNum) -> {
                        TripResponse tripResponse = new TripResponse();
                        Timestamp timestamp = rs.getTimestamp("start_time");
                        tripResponse.setTrip_id(rs.getInt("trip_id"));
                        tripResponse.setStart_location(rs.getString("start_location"));
                        tripResponse.setEnd_location(rs.getString("end_location"));
                        tripResponse.setStart_time(timestamp.toInstant());
                        tripResponse.setPrice(rs.getInt("price"));
                        tripResponse.setCoach_id(rs.getInt("coach_id"));
                        tripResponse.setPrice(rs.getInt("price"));
                        tripResponse.setCoach_id(rs.getInt("coach_id"));
                        tripResponse.setCoach_type(rs.getString("coach_type"));
                        tripResponse.setTotal_seat(rs.getInt("total_seat"));
                        return tripResponse;
                    }
            );
            int total = jdbcTemplate.queryForObject(countSQL, Integer.class);
            Map<String, Object> map = new HashMap<>();
            map.put("trips", tripsList);
            map.put("total", total);
            return map;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL: " + e.getMessage());
            return null;
        } catch (DataAccessException e) {
            System.out.println("Lỗi truy cập CSDL: " + e.getMessage());
            return null;
        }
    }
}
