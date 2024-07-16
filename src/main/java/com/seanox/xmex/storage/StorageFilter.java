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

import com.seanox.xmex.util.Codec;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
@Order(2)
class StorageFilter extends HttpFilter {

    @Autowired
    private StorageService storageService;

    /**
     * Pattern for the Storage header
     *     Group 0. Full match
     *     Group 1. Storage
     *     Group 2. Name of the root element (optional)
     */
    private static final Pattern PATTERN_HEADER_STORAGE = Pattern
            .compile("^(\\w{1,64})(?:\\s+(\\w+)){0,1}$");

    private void doConnect(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        // TODO:
    }

    private void doDelete(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        // TODO:
    }

    private void doGet(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        // TODO:
    }

    private void doOptions(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        // TODO:
    }

    private void doPatch(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        // TODO:
    }

    private void doPost(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        // TODO:
    }

    private void doPut(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        // TODO:
    }

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {

        final String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        if (!requestUri.startsWith(this.storageService.getServiceUri())) {
            chain.doFilter(request, response);
            return;
        }

        final String requestMethod = request.getMethod().toUpperCase();

        try {

            // Access-Control headers are received during preflight OPTIONS request
            if (requestMethod.equals(HttpMethod.OPTIONS)
                    && Objects.nonNull(request.getHeader(HttpHeader.ORIGIN))
                    && Objects.isNull(request.getHeader(HttpHeader.STORAGE)))
                throw new NoContentState();

            final String storageIdentifier = request.getHeader(HttpHeader.STORAGE);
            if (Objects.isNull(storageIdentifier))
                throw new BadRequestState(
                        new HttpHeader(HttpHeader.MESSAGE, "Missing storage identifier"));
            if (!PATTERN_HEADER_STORAGE.matcher(storageIdentifier).matches())
                throw new BadRequestState(
                        new HttpHeader(HttpHeader.MESSAGE, "Invalid storage identifier"));

            final StringBuilder xpathBuilder = new StringBuilder();
            xpathBuilder.append(requestUri);
            if (Objects.nonNull(request.getQueryString()))
                xpathBuilder.append("?").append(URLDecoder.decode(request.getQueryString(), Charset.defaultCharset()));
            String xpath = xpathBuilder.substring(this.storageService.getServiceUri().length());
            if (Codec.PATTERN_BASE64.matcher(xpath).matches())
                xpath = Codec.decodeBase64(xpath, StandardCharsets.UTF_8);
            else if (Codec.PATTERN_HEX.matcher(xpath).matches())
                xpath = Codec.decodeHex(xpath, StandardCharsets.UTF_8);

            // Except CONNECT, OPTIONS and POST, all requests expect an XPath or
            // XPath function. CONNECT and OPTIONS do not use an (X)Path to
            // establish a storage. POST uses the XPath for transformation only
            // optionally to delimit the XML data for the transformation and
            // works also without. In the other cases an empty XPath is replaced
            // by the root slash.
            if (xpath.isBlank()
                    && !requestMethod.equals(HttpMethod.CONNECT)
                    && !requestMethod.equals(HttpMethod.OPTIONS)
                    && !requestMethod.equals(HttpMethod.POST))
                xpath = "/";

            switch (request.getMethod().toUpperCase()) {
                case HttpMethod.CONNECT:
                    this.doConnect(request, response, storageIdentifier, xpath);
                case HttpMethod.DELETE:
                    this.doDelete(request, response, storageIdentifier, xpath);
                case HttpMethod.GET:
                    this.doGet(request, response, storageIdentifier, xpath);
                case HttpMethod.OPTIONS:
                    this.doOptions(request, response, storageIdentifier, xpath);
                case HttpMethod.PATCH:
                    this.doPatch(request, response, storageIdentifier, xpath);
                case HttpMethod.POST:
                    this.doPost(request, response, storageIdentifier, xpath);
                case HttpMethod.PUT:
                    this.doPut(request, response, storageIdentifier, xpath);
                default:
                    throw new MethodNotAllowedState(
                            new HttpHeader(HttpHeader.ALLOW,
                                    String.join(", ", HttpMethod.listAllowedMethods())));
            }
        } catch (final State state) {
            // TODO:
        }
    }

    private abstract static class State extends Error {
    }

    private abstract static class AbstractHttpState extends State {

        private final int httpStatus;

        private final HttpHeader[] httpHeaders;

        private AbstractHttpState(final int httpStatus, final HttpHeader... httpHeaders) {
            this.httpStatus = httpStatus;
            this.httpHeaders = httpHeaders;
        }
    }

    private static class MethodNotAllowedState extends AbstractHttpState {
        private MethodNotAllowedState(final HttpHeader... httpHeaders) {
            super(HttpServletResponse.SC_METHOD_NOT_ALLOWED, httpHeaders);
        }
    }

    private static class BadRequestState extends AbstractHttpState {
        private BadRequestState(final HttpHeader... httpHeaders) {
            super(HttpServletResponse.SC_BAD_REQUEST, httpHeaders);
        }
    }

    private static class NoContentState extends AbstractHttpState {
        private NoContentState(final HttpHeader... httpHeaders) {
            super(HttpServletResponse.SC_NO_CONTENT, httpHeaders);
        }
    }

    @AllArgsConstructor(access=AccessLevel.PRIVATE)
    private static class HttpHeader {

        private static final String ORIGIN = HttpHeaders.ORIGIN;
        private static final String STORAGE = "Storage";
        private static final String ALLOW = "Allow";
        private static final String MESSAGE = "Message";

        private final String header;

        private final String value;
    }

    private static class HttpMethod {

        private static final String CONNECT = "CONNECT";
        private static final String OPTIONS = "OPTIONS";
        private static final String GET = "GET";
        private static final String POST = "POST";
        private static final String PUT = "PUT";
        private static final String PATCH = "PATCH";
        private static final String DELETE = "DELETE";

        private static String[] listAllowedMethods() {
            return new String[] {CONNECT, OPTIONS, GET, POST, PUT, PATCH, DELETE};
        }
    }
}
