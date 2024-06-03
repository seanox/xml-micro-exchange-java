/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der Apache License.
 *
 * XMEX XML-Micro-ExchangExchange
 * Copyright (C) 2024 Seanox Software Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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

        // If the HTTPS connector is used, all HTTP requests, with exception of
        // ACME requests, are redirected to HTTPS.
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
