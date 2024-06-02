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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test-2"})
class AcmeFilterTest_2 {

    @Autowired
    private MockMvc mockMvc;

    @Value("${acme.hash}")
    private String acmeHash;

    @Value("${acme.token.uri}")
    private String acmeTokenUri;

    @Test
    void testAcme_01()
            throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.GET, URI.create(this.acmeTokenUri));
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(this.acmeHash));
    }

    @Test
    void testAcme_02()
            throws Exception {
        for (final HttpMethod method : new HttpMethod[] {
                HttpMethod.DELETE,
                HttpMethod.HEAD,
                HttpMethod.OPTIONS,
                HttpMethod.PATCH,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.TRACE
        }) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(method, URI.create(this.acmeTokenUri));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        }
    }

    @Test
    void testAcmeSecure_01()
            throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.GET, URI.create(this.acmeTokenUri))
                .secure(true);
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testAcmeSecure_02()
            throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.TRACE, URI.create(this.acmeTokenUri))
                .secure(true);
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAcmeSecure_03()
            throws Exception {
        for (final HttpMethod method : new HttpMethod[] {
                HttpMethod.DELETE,
                HttpMethod.HEAD,
                HttpMethod.OPTIONS,
                HttpMethod.PATCH,
                HttpMethod.POST,
                HttpMethod.PUT
        }) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(method, URI.create(this.acmeTokenUri))
                    .secure(true);
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }
}
