/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der Apache License.
 *
 * XMEX XML-Micro-Exchange
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

import com.seanox.xmex.storage.StorageService.StorageIdentifierException;
import com.seanox.xmex.storage.StorageService.StorageInsufficientException;
import com.seanox.xmex.storage.StorageService.StorageMeta;
import com.seanox.xmex.util.Codec;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Order(2)
class StorageFilter extends HttpFilter {

    @Autowired
    private StorageService storageService;

    @Value("${storage.uri}")
    private String storageServiceUri;

    @Value("#{T(com.seanox.xmex.util.Number).parseLong('${spring.servlet.multipart.max-request-size}')}")
    private long storageServiceMaxRequestSize;

    private void doConnect(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws IOException {
        if (Strings.isNotEmpty(xpath))
            throw new BadRequestState(new HttpHeader(HttpHeader.MESSAGE, "Unexpected XPath"));
        try (final StorageMeta storageMeta = this.storageService.touch(storageIdentifier)) {
            if (storageMeta.getUnique().equals(storageMeta.getRevision()))
                throw new CreatedState(storageMeta);
            throw new NoContentState(storageMeta);
        } catch (final StorageInsufficientException exception) {
            throw new InsufficientStorageState();
       }
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

    private byte[] readRequestPayload(final HttpServletRequest request, final String... acceptContentTypes)
            throws IOException {
        if (request.getContentLength() <= 0)
            throw new ContentLengthRequiredState();
        final int requestContentLength = request.getContentLength();
        if (requestContentLength > Integer.MAX_VALUE
                || requestContentLength >= this.storageServiceMaxRequestSize)
            throw new ContentTooLargeState();
        // TODO: check acceptContentTypes
        final byte[] requestPayload = new byte[requestContentLength];
        if (request.getInputStream().read(requestPayload)
                != requestContentLength)
            throw new BadRequestState(new HttpHeader(HttpHeader.MESSAGE, "Conflicting content length"));
        return requestPayload;
    }

    private void doPatch(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        final byte[] requestPayload = this.readRequestPayload(request);
        // TODO:
    }

    private void doPost(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        final byte[] requestPayload = this.readRequestPayload(request);
        // TODO:
    }

    private void doPut(final HttpServletRequest request, final HttpServletResponse response,
                    final String storageIdentifier, final String xpath)
            throws ServletException, IOException {
        final byte[] requestPayload = this.readRequestPayload(request);
        // TODO:
    }

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {

        final String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        if (!requestUri.startsWith(this.storageServiceUri)) {
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

            final StringBuilder xpathBuilder = new StringBuilder();
            xpathBuilder.append(requestUri);
            if (Objects.nonNull(request.getQueryString()))
                xpathBuilder.append("?").append(URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8));
            String xpath = xpathBuilder.substring(this.storageServiceUri.length());
            if (Codec.PATTERN_BASE64.matcher(xpath).matches())
                xpath = Codec.decodeBase64(xpath, StandardCharsets.UTF_8);
            else if (Codec.PATTERN_HEX.matcher(xpath).matches())
                xpath = Codec.decodeHex(xpath, StandardCharsets.UTF_8);
            xpath = xpath.trim();

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

            final String storageIdentifier = request.getHeader(HttpHeader.STORAGE);

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
        } catch (final StorageIdentifierException storageIdentifierException) {
            throw new BadRequestState(
                    new HttpHeader(HttpHeader.MESSAGE, storageIdentifierException.getMessage()));
        } catch (final State state) {
            // TODO:
        }
    }

    private abstract static class State extends Error {
    }

    private abstract static class AbstractHttpState extends State {

        private final int httpStatus;

        private final List<HttpHeader> httpHeaders;

        private AbstractHttpState(final int httpStatus, final StorageMeta storageMeta, final HttpHeader... httpHeaders) {
            this.httpStatus = httpStatus;
            this.httpHeaders = Arrays.asList(httpHeaders);
            if (Objects.isNull(storageMeta))
                return;

            // TODO: Storage:
            // TODO: Storage-Revision:
            // TODO: Storage-Space:
            // TODO: Storage-Last-Modified:
            // TODO: Storage-Expiration:
            // TODO: Storage-Expiration-Time:
            // TODO: Execution-Time:
        }

        private AbstractHttpState(final int httpStatus, final HttpHeader... httpHeaders) {
            this(httpStatus, null, httpHeaders);
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

        private NoContentState(final StorageMeta storageMeta, final HttpHeader... httpHeaders) {
            super(HttpServletResponse.SC_NO_CONTENT, storageMeta, httpHeaders);
        }
    }

    private static class CreatedState extends AbstractHttpState {
        private CreatedState(final HttpHeader... httpHeaders) {
            super(HttpServletResponse.SC_CREATED, httpHeaders);
        }

        private CreatedState(final StorageMeta storageMeta, final HttpHeader... httpHeaders) {
            super(HttpServletResponse.SC_CREATED, storageMeta, httpHeaders);
        }
    }

    private static class InsufficientStorageState extends AbstractHttpState {
        private InsufficientStorageState(final HttpHeader... httpHeaders) {
            super(507, httpHeaders);
        }
    }

    private static class ContentLengthRequiredState extends AbstractHttpState {
        private ContentLengthRequiredState(final HttpHeader... httpHeaders) {
            super(411, httpHeaders);
        }
    }

    private static class ContentTooLargeState extends AbstractHttpState {
        private ContentTooLargeState(final HttpHeader... httpHeaders) {
            super(413, httpHeaders);
        }
    }

    private static class UnsupportedMediaTypeState extends AbstractHttpState {
        private UnsupportedMediaTypeState(final HttpHeader... httpHeaders) {
            super(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, httpHeaders);
        }
    }

    @AllArgsConstructor(access=AccessLevel.PRIVATE)
    private static class HttpHeader {

        private static final String ORIGIN = HttpHeaders.ORIGIN;
        private static final String STORAGE = "Storage";
        private static final String ALLOW = HttpHeaders.ALLOW;
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
