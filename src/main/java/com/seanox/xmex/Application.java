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
package com.seanox.xmex;

import jakarta.annotation.PostConstruct;
import org.apache.catalina.valves.AccessLogValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
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

    @Component
    private static class ServerCustomizer implements WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> {

        @Autowired
        private ServerProperties serverProperties;

        // If the log directory (server.tomcat.accesslog.directory) is a
        // relative path, it should be based on the current working directory.
        // By default, the embedded Tomcat may be executed below the temp
        // directory of the user and the log directory, if it is a relative
        // path, is based on this directory.

        // Implementation is based on:
        // org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer
        //     private void customizeAccessLog(ConfigurableTomcatWebServerFactory factory)

        // Only the existing AccessLogValve is modified, but no new one is added.

        // Especially for Windows, the isAbsolute method must be used for paths,
        // which recognizes the drive letters and whether the path begins with a
        // (back)slash, where the isAbsolute method otherwise returns false.

        @Override
        public void customize(final ConfigurableTomcatWebServerFactory factory) {
            final ServerProperties.Tomcat serverProperties = this.serverProperties.getTomcat();
            final ServerProperties.Tomcat.Accesslog accessLog = serverProperties.getAccesslog();
            if ((StringUtils.hasText(accessLog.getDirectory())
                        && accessLog.getDirectory().matches("^\\s*[\\/].*"))
                    || new File(accessLog.getDirectory()).isAbsolute())
                return;
            final File accessLogDirectory = new File(".", accessLog.getDirectory()).getAbsoluteFile();
            ((TomcatServletWebServerFactory)factory).getEngineValves().stream()
                    .filter(engineValve -> engineValve instanceof AccessLogValve)
                    .findFirst()
                    .ifPresent(value ->
                            ((AccessLogValve) value).setDirectory(accessLogDirectory.toString()));
        }
    }
}
