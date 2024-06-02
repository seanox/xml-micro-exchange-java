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

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("('${acme.port:}').matches('^\\d+$')"
        + " && !('${acme.port:}').matches('^0+$')"
        + " && ('${server.ssl.enabled:}').matches('^(on|true)$')")
class AcmeConnector {

    @Autowired
    private AcmeService acmeService;

    // The ACME HTTP-01 challenge is a support if the application runs via HTTPS
    // and an Automatic Certificate Management Environment is (ACME) used. Only
    // then the HTTP connector and the filter for AMCE requests are activated.

    @Bean
    private WebServerFactoryCustomizer<TomcatServletWebServerFactory> addAcmeHttpConnectorCustomizer() {
        return (final TomcatServletWebServerFactory factory) -> {
            final Connector connector = new Connector();
            connector.setPort(this.acmeService.getAcmePort());
            factory.addAdditionalTomcatConnectors(connector);
        };
    }
}
