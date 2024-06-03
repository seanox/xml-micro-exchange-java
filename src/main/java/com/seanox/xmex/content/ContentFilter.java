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
package com.seanox.xmex.content;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@Order(4)
class ContentFilter extends HttpFilter {

    @Autowired
    private ContentService contentService;

    // The filter mainly modifies/manipulates the request URI if it refers to a
    // directory. Then the URL should end with a slash and if there is a default
    // file, its content should be output without a redirect to the default file
    // being sent to the client.

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest contentRequest = request;
        final String contentRequestUri = request.getRequestURI();
        final String contentRequestPath = this.contentService.getContentEntryPath(request);
        final File contentRequestEntry = this.contentService.getContentEntry(contentRequestPath);
        if (contentRequestEntry.isDirectory()) {
            if (!contentRequestUri.endsWith("/")) {
                response.sendRedirect(String.format("%s/", contentRequestUri));
                return;
            }
            final File contentDirectoryDefault = this.contentService.getContentDirectoryDefault(contentRequestPath);
            if (Objects.nonNull(contentDirectoryDefault)) {
                contentRequest = new HttpServletRequestWrapper(contentRequest) {
                    @Override
                    public String getRequestURI() {
                        return String.format("%s%s",
                                contentRequestUri,
                                URLEncoder.encode(contentDirectoryDefault.getName(),
                                        StandardCharsets.UTF_8));
                    }
                };
            }
        }
        chain.doFilter(contentRequest, response);
    }
}
