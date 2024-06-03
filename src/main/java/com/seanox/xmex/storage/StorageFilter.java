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
package com.seanox.xmex.storage;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@Order(1)
class StorageFilter extends HttpFilter {

    @Autowired
    private StorageService storageService;

    @Value("#{new Boolean(('${server.ssl.enabled:}').matches('^(on|true)$'))}")
    private boolean isSecureConnection;

    // If the HTTPS Connector is used, all HTTP requests are redirected to HTTPS.

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {

        final String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        if (!requestUri.startsWith(this.storageService.getServiceUri())) {
            chain.doFilter(request, response);
            return;
        }

        if (!request.isSecure()
                && isSecureConnection) {
            final StringBuilder requestUrl = new StringBuilder(request.getRequestURL().toString());
            final String queryString = request.getQueryString();
            if (Objects.nonNull(queryString)
                    && !queryString.isBlank())
                requestUrl.append("?").append(queryString);
            response.sendRedirect(requestUrl.toString()
                    .replaceAll("^(?i)(http)(://)", "$1s$2"));
            return;
        }
        try {
            switch (request.getMethod().toUpperCase()) {
                case "DELETE":
                    this.doDelete(storage, xpath, request, response);
                case "GET":
                    this.doGet(storage, xpath, request, response);
                case "OPTIONS":
                    this.doOptions(storage, xpath, request, response);
                case "PATCH":
                    this.doPatch(storage, xpath, request, response);
                case "POST":
                    this.doPost(storage, xpath, request, response);
                case "PUT":
                    this.doPut(storage, xpath, request, response);
                default:
                    throw new MethodNotAllowedState(
                            new HttpHeader("Allow", "OPTIONS, GET, POST, PUT, PATCH, DELETE")
                    );
            }
        } catch (Throwable throwable) {
        }
    }

    private abstract static class State extends Error {
    }

    private static class MethodNotAllowedState extends State {

        final private static int httpStatus = HttpServletResponse.SC_METHOD_NOT_ALLOWED;

        final private HttpHeader[] httpHeaders;

        private MethodNotAllowedState(final HttpHeader... httpHeaders) {
            this.httpHeaders = httpHeaders;
        }
    }

    @AllArgsConstructor(access=AccessLevel.PRIVATE)
    private static class HttpHeader {

        final private String header;

        final private String value;
    }
}
