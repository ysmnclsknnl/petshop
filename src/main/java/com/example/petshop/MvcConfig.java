package com.example.petshop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/pets").setViewName("pets");
        registry.addViewController("/pets/add").setViewName("add-pet-form");
        registry.addViewController("/register").setViewName("login");
    }
}
