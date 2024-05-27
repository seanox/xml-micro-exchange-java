/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der Apache License.
 *
 * XML Micro Exchange
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
class ContentConfigurer implements WebMvcConfigurer {

    @Autowired
    private ContentService contentService;

    // Handle static content in Spring Boot
    // https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/static-resources.html
    // https://www.baeldung.com/spring-mvc-static-resources

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        String contentDirectoryPath = this.contentService.getContentDirectoryPath();
        if (System.getProperty("os.name").matches("(?i)^windows\\b.*"))
            contentDirectoryPath = String.format("///%s",
                    contentDirectoryPath.replace('\\', '/'));
        if (!contentDirectoryPath.endsWith("/"))
            contentDirectoryPath = String.format("%s/", contentDirectoryPath);
        registry.addResourceHandler("/**")
                .addResourceLocations(String.format("file:%s", contentDirectoryPath));
    }
}
