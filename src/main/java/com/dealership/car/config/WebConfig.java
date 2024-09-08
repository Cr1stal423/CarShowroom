package com.dealership.car.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig class implements WebMvcConfigurer to configure view controllers without the need for explicit controller classes.
 * This configuration maps specific URL paths directly to view names.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{
    /**
     * Adds view controllers to the ViewControllerRegistry to map specific URL paths directly to view names.
     *
     * @param registry the ViewControllerRegistry to which view controllers are to be added
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("index");
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/services").setViewName("services");
        registry.addViewController("/login").setViewName("login");
    }
}
