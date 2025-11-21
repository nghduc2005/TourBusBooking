package com.csdl.tourbusbooking.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String DB_URL =
			"jdbc:mysql://trolley.proxy.rlwy.net:47446/railway"
		  + "?useUnicode=true&characterEncoding=UTF-8"
		  + "&useSSL=true&requireSSL=true"
		  + "&serverTimezone=Asia/Ho_Chi_Minh";
	private static final String USER = "root";
	private static final String PASS = "qUynYbaIixomsEupdCqSOEGKbDeBYMjV";
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
