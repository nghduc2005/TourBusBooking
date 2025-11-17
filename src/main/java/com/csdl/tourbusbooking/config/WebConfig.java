package com.csdl.tourbusbooking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthSession authSession;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authSession)
                .addPathPatterns("/**")               // áp dụng cho tất cả endpoint
                .excludePathPatterns("/login",
                        "/register",
                        "/error");
    }
}
