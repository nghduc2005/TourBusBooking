package com.csdl.tourbusbooking.service;

import com.csdl.tourbusbooking.constant.UserConstant;
import com.csdl.tourbusbooking.dto.*;
import com.csdl.tourbusbooking.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
        String sql = "select * from accounts where username= ?"; //tìm tài khoản đã tồn tại chưa
        try{
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{username},
                    (rs, rowNum) -> {
                        Account account = new Account();
                        account.setUsername(rs.getString("username"));
                        account.setPassword(rs.getString("password"));
                        account.setPhone(rs.getString("phone"));
                        account.setAddress(rs.getString("address"));
                        account.setRole(rs.getString("role"));
                        account.setName(rs.getString("name"));
                        account.setNote(rs.getString("note"));
                        return account;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null; //không tìm thấy
        }
    }
    public Boolean findAccountById(int id) {
        String sql = "select username from accounts where account_id= ?";
        try {
            String usernameAtDB = jdbcTemplate.queryForObject(sql,String.class, id);
            return usernameAtDB!=null;
        } catch (EmptyResultDataAccessException e) {
            return false; //không tìm thấy
        }
    }
    public Account getProfile(String username) {
        String sql = "select account_id, name, phone, address, role from accounts where username= ?";
        try{
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{username},
                    new RowMapper<Account>() {
                        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Account account = new Account();
                            account.setAccount_id(rs.getInt("account_id"));
                            account.setName(rs.getString("name"));
                            account.setPhone(rs.getString("phone"));
                            account.setAddress(rs.getString("address"));
                            account.setRole(rs.getString("role"));
                            return account;
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null; //không tìm thấy
        }
    }

    public Account getProfileById(String id) {
        String sql = "select name, phone, address, role from accounts where account_id= ?";
        try{
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    new RowMapper<Account>() {
                        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Account account = new Account();
                            account.setName(rs.getString("name"));
                            account.setPhone(rs.getString("phone"));
                            account.setAddress(rs.getString("address"));
                            account.setRole(rs.getString("role"));
                            return account;
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null; //không tìm thấy
        }
    }
    public Boolean createAccount(RegisterRequest request) {
        String sql = "insert into accounts(username,password, name, address, phone, role) values(?," +
                "?, ?, ?, ?, ?)";
        try{
            jdbcTemplate.update(
                    sql,
                    request.getUsername(),
                    request.getPassword(),
                    request.getName(),
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
    public Boolean changePassword(ChangePasswordRequest request, String username) {
        String sql =  "update accounts set password = ? where username = ?";
        String password = request.getPassword();
        try {
            int rows = jdbcTemplate.update(sql,password,username);
            if(rows == 1) {
                System.out.println("Đổi mật khẩu thành công!");
                return true;
            }
            System.out.println("Không tìm thấy user");
            return false;
        } catch (DataIntegrityViolationException e) {
            //trùng khóa chính/vi phạm khóa ngoại/null tại not null/vượt kích thước định nghĩa
            System.out.println("Lỗi ràng buộc dữ liệu");
            return false;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL");
            return false;
        }
    }
    public Boolean changePasswordById(ChangePasswordRequest request, int id) {
        String sql =  "update accounts set password = ? where account_id = ?";
        String password = request.getPassword();
        try {
            int rows = jdbcTemplate.update(sql,password, id);
            if(rows == 1) {
                System.out.println("Đổi mật khẩu thành công!");
                return true;
            }
            System.out.println("Không tìm thấy user");
            return false;
        } catch (DataIntegrityViolationException e) {
            //trùng khóa chính/vi phạm khóa ngoại/null tại not null/vượt kích thước định nghĩa
            System.out.println("Lỗi ràng buộc dữ liệu");
            return false;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL");
            return false;
        }
    }
    public Boolean changeProfile(ChangeProfileRequest request, String username) {
        String sql =  "update accounts set name = ?, phone = ?, address = ?, note = ? where username = ?";
        String name = request.getName();
        String phone = request.getPhone();
        String address = request.getAddress();
        String note = request.getNote();
        try {
            int rows = jdbcTemplate.update(sql,name, phone, address, note,username);
            if(rows == 1) {
                System.out.println("Cập nhật thông tin thành công!");
                return true;
            }
            System.out.println("Không tìm thấy user");
            return false;
        } catch (DataIntegrityViolationException e) {
            //trùng khóa chính/vi phạm khóa ngoại/null tại not null/vượt kích thước định nghĩa
            System.out.println("Lỗi ràng buộc dữ liệu");
            return false;
        } catch (BadSqlGrammarException e) {
            System.out.println("Sai cú pháp SQL");
            return false;
        }
    }
    public Boolean createAccountAdmin(CreateAccountRequest request) {
        String sql = "insert into accounts(username,password, name, address, phone, role, note) values(?," +
                "?, ?, ?, ?, ?, ?)";
        try{
            jdbcTemplate.update(
                    sql,
                    request.getUsername(),
                    request.getPassword(),
                    request.getName(),
                    request.getAddress(),
                    request.getPhone(),
                    request.getRole(),
                    request.getNote()
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
    public Boolean editAccountAdmin(ChangeProfileRequest request, int id) {
        String sql =  "update accounts set name = ?, phone = ?, address = ?, note = ? where account_id = ?";
        try{
            jdbcTemplate.update(
                    sql,
                    request.getName(),
                    request.getPhone(),
                    request.getAddress(),
                    request.getNote(),
                    id
            );
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
    public Boolean deletedById(int id) {
        String sql =  "delete from accounts where account_id = ?";
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
    public int deleteByIds(List<Long> ids) {
        String sql = "DELETE FROM accounts WHERE account_id IN (" +
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
    public  Map<String, Object>  getAllAccounts(String username, int current, int pageSize) {
        String sql = "select account_id, name, address, phone, username, role from accounts where username != ? order" +
                " by account_id " +
                "limit ? offset ?";
        String countSQL = "select count(*) total from accounts where username != ?";
        try{
            List<AccountResponse> accountList = jdbcTemplate.query(
                    sql,
                    new Object[]{username, pageSize, (current-1)*pageSize},
                    (rs, rowNum) -> {
                        AccountResponse accountResponse = new AccountResponse();
                        accountResponse.setAccount_id(rs.getInt("account_id"));
                        accountResponse.setName(rs.getString("name"));
                        accountResponse.setAddress(rs.getString("address"));
                        accountResponse.setPhone(rs.getString("phone"));
                        accountResponse.setUsername(rs.getString("username"));
                        accountResponse.setRole(rs.getString("role"));
                        return accountResponse;
                    }
            );
            int total = jdbcTemplate.queryForObject(countSQL, Integer.class, username);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("accounts", accountList);
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
