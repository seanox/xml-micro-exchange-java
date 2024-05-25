package com.seanox.xmex.datasource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

public class DatasourceFilter extends HttpFilter {

    private static final int FILTER_ORDER = 1;

    @Override
    public void init(final FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
    }

    @Bean
    FilterRegistrationBean datasourceFilterRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DatasourceFilter());
        registration.setOrder(FILTER_ORDER);
        registration.addUrlPatterns("/xmex!*");
        return registration;
    }
}
