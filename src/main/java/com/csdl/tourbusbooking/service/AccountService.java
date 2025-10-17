package com.csdl.tourbusbooking.service;

import com.csdl.tourbusbooking.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JdbcTemplate jdbcTemplate;

    public Long register(Account account) {
        String sql = "INSERT INTO accounts (username, password, fullname, address, phone, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getFullname());
            ps.setString(4, account.getAddress());
            ps.setString(5, account.getPhone());
            ps.setString(6, account.getRole());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
