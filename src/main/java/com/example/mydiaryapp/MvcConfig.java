package com.example.mydiaryapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("main/home");
        registry.addViewController("/").setViewName("main/home");
        registry.addViewController("/login").setViewName("main/login");
        registry.addViewController("/register").setViewName("main/register");
    }

}
