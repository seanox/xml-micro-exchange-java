package com.seanox.xmex.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@ActiveProfiles({"test-http"})
class ContentFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetDirectory_01()
            throws Exception {
        for (final String path : new String[] {"/x", "/x/xx", "/x/xx/xxx"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.GET, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(MockMvcResultMatchers.redirectedUrl(path + "/"));
        }
    }

    @Test
    void testGetDirectory_02()
            throws Exception {
        for (final String path : new String[] {"/", "/x/", "/x/xx/", "/x/xx/xxx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.GET, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}
