package com.gwj.controller;

import com.gwj.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired private SetupInterceptor setupInterceptor;

  @Autowired private AdminInterceptor adminInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // SetupInterceptor executa antes de todos para garantir o fluxo de primeiro boot
    registry.addInterceptor(setupInterceptor).order(1);

    String prefix = AppConfig.ADMIN_ROUTE_PREFIX;
    registry
        .addInterceptor(adminInterceptor)
        .addPathPatterns("/" + prefix + "/**")
        .excludePathPatterns("/" + prefix + "/login")
        .order(2);
  }
}
