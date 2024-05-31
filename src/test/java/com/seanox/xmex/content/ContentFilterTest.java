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
        for (final String path : new String[] {"/", "/x/", "/x/xx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.GET, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Test
    void testGetDirectory_03()
            throws Exception {
        for (final String path : new String[] {"/x/xx/xxx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.GET, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }

    @Test
    void testGetDirectoryDefault_01()
            throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.GET, URI.create("/"));
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("index.html"));
    }

    @Test
    void testGetDirectoryDefault_02()
            throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.GET, URI.create("/x/"));
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("index.htm"));
    }

    @Test
    void testGetDirectoryDefault_03()
            throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(HttpMethod.GET, URI.create("/x/xx/"));
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("index.xhtml"));
    }

    @Test
    void testHeadDirectory_01()
            throws Exception {
        for (final String path : new String[] {"/", "/x/", "/x/xx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.HEAD, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(""));
        }
    }

    @Test
    void testOptionsDirectory_01()
            throws Exception {
        for (final String path : new String[] {"/", "/x/", "/x/xx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.OPTIONS, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(""));
        }
    }

    @Test
    void testPostDirectory_01()
            throws Exception {
        for (final String path : new String[] {"/", "/x/", "/x/xx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.POST, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        }
    }

    @Test
    void testPutDirectory_01()
            throws Exception {
        for (final String path : new String[] {"/", "/x/", "/x/xx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.PUT, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        }
    }

    @Test
    void testDeleteDirectory_01()
            throws Exception {
        for (final String path : new String[] {"/", "/x/", "/x/xx/"}) {
            final RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .request(HttpMethod.DELETE, URI.create(path));
            this.mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        }
    }
}
