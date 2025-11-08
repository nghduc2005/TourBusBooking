package com.csdl.tourbusbooking.service;

import com.csdl.tourbusbooking.dto.AccountResponse;
import com.csdl.tourbusbooking.dto.CoachRequest;
import com.csdl.tourbusbooking.dto.CoachResponse;
import com.csdl.tourbusbooking.model.Account;
import com.csdl.tourbusbooking.model.Coach;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachService {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    public boolean create(CoachRequest request) {
        String coach_name = request.getCoach_name();
        String coach_type = request.getCoach_type();
        int total_seat = request.getTotal_seat();
        String status =  request.getStatus();
        String sql = "insert into coachs(coach_name,coach_type, total_seat, status) values(?, ?, ?, ?)";
        System.out.println(coach_name + " " +  coach_type + " " + total_seat + " " + status);
        try{
            jdbcTemplate.update(
                    sql,
                    coach_name,
                    coach_type,
                    total_seat,
                    status
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

    public boolean changeDetail(CoachRequest request, String id){
        String sql =  "update coachs set coach_name = ?, coach_type = ?, total_seat = ?, status = ? where coach_id " +
                "=" +
                " ?";
        try{
            int rows = jdbcTemplate.update(
                    sql,
                    request.getCoach_name(),
                    request.getCoach_type(),
                    request.getTotal_seat(),
                    request.getStatus(),
                    id
            );
            if(rows == 0){
                return false;
            }
            return true;
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
    public CoachResponse getDetailById(String id) {
        String sql = "select coach_name, coach_type, total_seat, status from coachs where coach_id= ?";
        try{
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    new RowMapper<CoachResponse>() {
                        public CoachResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                            CoachResponse coach = new CoachResponse();
                            coach.setCoach_name(rs.getString("coach_name"));
                            coach.setCoach_type(rs.getString("coach_type"));
                            coach.setTotal_seat(rs.getInt("total_seat"));
                            coach.setStatus(rs.getString("status"));
                            return coach;
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null; //không tìm thấy
        }
    }
    public Boolean deleteById(String id) {
        String sql =  "delete from coachs where coach_id = ?";
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
        String sql = "DELETE FROM coachs WHERE coach_id IN (" +
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

    public Map<String, Object>  getAllCoachs(int current, int pageSize) {
        String sql = "select coach_id, coach_name, coach_type, total_seat, status from coachs order by coach_id limit" +
                " ? offset ?";
        String countSQL = "select count(*) total from coachs";
        try{
            List<CoachResponse> coachs = jdbcTemplate.query(
                    sql,
                    new Object[]{pageSize, (current-1)*pageSize},
                    (rs, rowNum) -> {
                        CoachResponse coachResponse = new CoachResponse();
                        coachResponse.setCoach_id(rs.getInt("coach_id"));
                        coachResponse.setCoach_name(rs.getString("coach_name"));
                        coachResponse.setCoach_type(rs.getString("coach_type"));
                        coachResponse.setTotal_seat(rs.getInt("total_seat"));
                        coachResponse.setStatus(rs.getString("status"));
                        return coachResponse;
                    }
            );
            int total = jdbcTemplate.queryForObject(countSQL, Integer.class);
            Map<String, Object> map =  new HashMap<>();
            map.put("coachs", coachs);
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
