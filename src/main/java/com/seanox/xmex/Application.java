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
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;
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

        // If the log directory (server.tomcat.accesslog.directory) is a relative
        // path, it should be based on the current working directory. By default,
        // the embedded Tomcat may be executed below the temp directory of the user
        // and the log directory, if it is a relative path, is based on this
        // directory.

        // Implementation is based on:
        // org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer
        //     private void customizeAccessLog(ConfigurableTomcatWebServerFactory factory)

        @Override
        public void customize(final ConfigurableTomcatWebServerFactory factory) {
            final ServerProperties.Tomcat serverProperties = this.serverProperties.getTomcat();
            if (Objects.isNull(serverProperties))
                return;
            final ServerProperties.Tomcat.Accesslog accessLogConfig = serverProperties.getAccesslog();
            if (Objects.isNull(accessLogConfig)
                    || !accessLogConfig.isEnabled()
                    || Objects.isNull(accessLogConfig.getDirectory())
                    || new File(accessLogConfig.getDirectory()).isAbsolute())
                return;
            final File accessLogDirectory = new File(".", accessLogConfig.getDirectory()).getAbsoluteFile();
            accessLogConfig.setDirectory(accessLogDirectory.toString());
            final AccessLogValve valve = new AccessLogValve();
            final PropertyMapper propertyMapper = PropertyMapper.get();
            propertyMapper.from(accessLogConfig.getConditionIf()).to(valve::setConditionIf);
            propertyMapper.from(accessLogConfig.getConditionUnless()).to(valve::setConditionUnless);
            propertyMapper.from(accessLogConfig.getPattern()).to(valve::setPattern);
            propertyMapper.from(accessLogConfig.getDirectory()).to(valve::setDirectory);
            propertyMapper.from(accessLogConfig.getPrefix()).to(valve::setPrefix);
            propertyMapper.from(accessLogConfig.getSuffix()).to(valve::setSuffix);
            propertyMapper.from(accessLogConfig.getEncoding()).whenHasText().to(valve::setEncoding);
            propertyMapper.from(accessLogConfig.getLocale()).whenHasText().to(valve::setLocale);
            propertyMapper.from(accessLogConfig.isCheckExists()).to(valve::setCheckExists);
            propertyMapper.from(accessLogConfig.isRotate()).to(valve::setRotatable);
            propertyMapper.from(accessLogConfig.isRenameOnRotate()).to(valve::setRenameOnRotate);
            propertyMapper.from(accessLogConfig.getMaxDays()).to(valve::setMaxDays);
            propertyMapper.from(accessLogConfig.getFileDateFormat()).to(valve::setFileDateFormat);
            propertyMapper.from(accessLogConfig.isIpv6Canonical()).to(valve::setIpv6Canonical);
            propertyMapper.from(accessLogConfig.isRequestAttributesEnabled()).to(valve::setRequestAttributesEnabled);
            propertyMapper.from(accessLogConfig.isBuffered()).to(valve::setBuffered);
            factory.addEngineValves(valve);
        }
    }
}
