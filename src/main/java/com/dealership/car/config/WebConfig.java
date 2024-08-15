package com.dealership.car.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("index");
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/services").setViewName("services");
        registry.addViewController("/gallery").setViewName("gallery");
        registry.addViewController("/login").setViewName("login");
    }
}
