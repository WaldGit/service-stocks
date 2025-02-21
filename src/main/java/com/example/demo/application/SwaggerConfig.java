package com.example.demo.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/students/**")
                .build();
    }
}
