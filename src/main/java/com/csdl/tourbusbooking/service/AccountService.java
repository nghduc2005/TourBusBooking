package com.csdl.tourbusbooking.service;

import com.csdl.tourbusbooking.constant.UserConstant;
import com.csdl.tourbusbooking.dto.RegisterRequest;
import com.csdl.tourbusbooking.model.Account;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    public Boolean findAccountByUsername(String username) {
        String sql = "select username from accounts where username= ?"; //tìm tài khoản đã tồn tại chưa
        try{
            String usernameAtDB = jdbcTemplate.queryForObject(sql,String.class, username);
            return usernameAtDB!=null; //tìm thấy và khác null
        } catch (EmptyResultDataAccessException e) {
            return false; //không tìm thấy
        }
    }
    public Account findAndReturnAccount(String username) {
        String sql = "select username, password from accounts where username= ?"; //tìm tài khoản đã tồn tại chưa
        try{
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{username},
                    (rs, rowNum) -> {
                        Account account = new Account();
                        account.setUsername(rs.getString("username"));
                        account.setPassword(rs.getString("password"));
                        return account;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null; //không tìm thấy
        }
    }
    public Boolean createAccount(RegisterRequest request) {
        String sql = "insert into accounts(username,password, fullname, address, phone, role) values(?," +
                "?, ?, ?, ?, ?)";
        try{
            jdbcTemplate.update(
                    sql,
                    request.getUsername(),
                    request.getPassword(),
                    request.getFullname(),
                    request.getAddress(),
                    request.getPhone(),
                    UserConstant.CUSTOMER_ROLE
            );
            return true;
        } catch (DataIntegrityViolationException e) {
            //trùng khóa chính/vi phạm khóa ngoại/null tại not null/vượt kích thước định nghĩa
            System.out.println("Lỗi ràng buộc dữ liệu");
            return false;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL");
            return false;
        }
    }


//    public Long register(Account account) {
//        String sql = "INSERT INTO accounts (username, password, fullname, address, phone, role) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(conn -> {
//            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, account.getUsername());
//            ps.setString(2, account.getPassword());
//            ps.setString(3, account.getFullname());
//            ps.setString(4, account.getAddress());
//            ps.setString(5, account.getPhone());
//            ps.setString(6, account.getRole());
//            return ps;
//        }, keyHolder);
//
//        return keyHolder.getKey().longValue();
//    }
}
