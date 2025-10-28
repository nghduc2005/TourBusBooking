package com.csdl.tourbusbooking.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthSession implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false); //không tạo mới nếu không có
        String path = request.getRequestURI();
        // Cho phép các API public (bổ sung sau)
        if (path.endsWith("/login") || path.endsWith("/register")) {
            return true;
        }
        //Chưa có session khi truy cập tài nguyên -> chặn
        if (session == null || session.getAttribute("account") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Chưa đăng nhập!");
            return false;
        }
        return true;
    }
}
