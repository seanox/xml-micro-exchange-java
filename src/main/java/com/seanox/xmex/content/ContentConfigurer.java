package com.seanox.xmex.content;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
class ContentConfigurer implements WebMvcConfigurer {

    // Handle static content in Spring Boot
    // https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/static-resources.html
    // https://www.baeldung.com/spring-mvc-static-resources

    // Use default pages in directories
    // https://stackoverflow.com/questions/28437314/spring-boot-doesnt-map-folder-requests-to-index-html-files/59052438#59052438

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    }
}
