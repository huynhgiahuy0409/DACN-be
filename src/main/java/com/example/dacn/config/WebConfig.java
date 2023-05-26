package com.example.dacn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub
        registry.addResourceHandler("/uploads/hotel-img/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/resources/uploads/hotel-img/");
        registry.addResourceHandler("/uploads/room-img/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/resources/uploads/room-img/");
    }
}
