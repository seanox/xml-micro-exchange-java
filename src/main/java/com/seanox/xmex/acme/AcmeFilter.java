/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der Apache License.
 *
 * XMEX XML-Micro-Exchang
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
package com.seanox.xmex.acme;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@Order(2)
@ConditionalOnExpression("('${acme.port:}').matches('^\\d+$')"
        + " && !('${acme.port:}').matches('^0+$')"
        + " && ('${server.ssl.enabled}').matches('^(on|true)$')")
class AcmeFilter extends HttpFilter {

    @Autowired
    private AcmeService acmeService;

    // The ACME challenge works for HTTP requests. If HTTPS is used, the ACME
    // challenge is ignored. It simply does not make sense.

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
        final String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        if (!request.isSecure()
                && requestUri.equals(acmeService.getAcmeTokenUri())) {
            if (HttpMethod.GET.matches(request.getMethod().toUpperCase())) {
                response.setStatus(HttpServletResponse.SC_OK);
                final PrintWriter responseWriter = response.getWriter();
                responseWriter.write(acmeService.getAcmeHash());
                responseWriter.flush();
            } else response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        chain.doFilter(request, response);
    }
}
