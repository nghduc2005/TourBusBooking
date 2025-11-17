package com.csdl.tourbusbooking.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthSession implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String method = request.getMethod();
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        if (path.endsWith("/login") || path.endsWith("/register")) {
            return true;
        }

        HttpSession session = request.getSession(false); // không tạo mới nếu không có

        if (session == null || session.getAttribute("account") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Chưa đăng nhập!");
            return false;
        }

        return true;
    }
}
