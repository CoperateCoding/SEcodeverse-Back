package com.coperatecoding.secodeverseback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.error.ErrorController;


@Configuration
public class WebConfig implements WebMvcConfigurer, ErrorController {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT",
                        HttpMethod.OPTIONS.name()
                );
    }

    @GetMapping({"/", "/error"})
    public String index() {
        return "index.html";
    }
}
