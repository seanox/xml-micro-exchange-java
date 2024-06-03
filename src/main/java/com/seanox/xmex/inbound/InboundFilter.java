package com.seanox.xmex.inbound;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@Order(1)
class InboundFilter extends HttpFilter {

    @Value("#{new Boolean(('${server.ssl.enabled:}').matches('^(on|true)$'))}")
    private boolean isSecureConnection;

    @Value("${acme.token.uri:}")
    private String acmeTokenUri;

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {

        final String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        final String queryString = request.getQueryString();

        if (!request.isSecure()
                && isSecureConnection
                && !requestUri.equals(acmeTokenUri)) {
            final StringBuilder requestUrl = new StringBuilder(request.getRequestURL().toString());
            if (Objects.nonNull(queryString)
                    && !queryString.isBlank())
                requestUrl.append("?").append(queryString);
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader(HttpHeaders.LOCATION, requestUrl.toString()
                    .replaceAll("^(?i)(http)(://)", "$1s$2"));
            return;
        }

        chain.doFilter(request, response);
    }
}
