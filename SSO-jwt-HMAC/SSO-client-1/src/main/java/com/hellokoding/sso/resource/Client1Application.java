package com.hellokoding.sso.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

/**
 * @author XieShaoping
 */
@SpringBootApplication
public class Client1Application {
    @Value("${services.auth}")
    private String authService;

    /**
     * 注册过滤器
     */
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<JwtFilter>();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.setInitParameters(Collections.singletonMap("services.auth", authService));
        registrationBean.addUrlPatterns("/protected-resource", "/logout");
        return registrationBean;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Client1Application.class, args);
    }
}

