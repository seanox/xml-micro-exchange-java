package com.seanox.xmex.acme;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

public class AcmeFilter extends HttpFilter {

    private static final int FILTER_ORDER = 2;

    @Override
    public void init(final FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
    }

    @Bean
    FilterRegistrationBean acmeFilterRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AcmeFilter());
        registration.setOrder(FILTER_ORDER);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
