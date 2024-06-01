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
 *
 * @author  Seanox Software Solutions
 * @version 1.4.0 20240521
 */
package com.seanox.xmex;

import jakarta.annotation.PostConstruct;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    // Enable http and https at the same time on Spring Boot
    // https://mvysny.github.io/spring-boot-enable-http-https/

    public static void main(final String... options) {
        final SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        springApplication.run(options);
    }

    @PostConstruct
    private void configureApplication() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    // The additional HTTP connector is only activated for HTTP-01 challenge if
    // the application is running with HTTPS and an ACME port has been defined.

    @Value("${acme.port:0}")
    private int acmePort;

    @Bean
    @ConditionalOnExpression("('${acme.port:}').matches('^\\d+$')"
            + " && !('${acme.port:}').matches('^0+$')"
            + " && ('${server.ssl.enabled}').matches('^(on|true)$')")
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> addAcmeHttpConnectorCustomizer() {
        return (final TomcatServletWebServerFactory factory) -> {
            final Connector connector = new Connector();
            connector.setPort(this.acmePort);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }
}
