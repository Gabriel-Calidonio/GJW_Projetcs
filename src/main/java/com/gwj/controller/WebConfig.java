package com.gwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SetupInterceptor setupInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // SetupInterceptor executa antes de todos para garantir o fluxo de primeiro boot
        registry.addInterceptor(setupInterceptor).order(1);

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/MRYnZpAsC9sp/**")
                .excludePathPatterns("/MRYnZpAsC9sp/login")
                .order(2);
    }
}
