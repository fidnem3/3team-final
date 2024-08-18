package com.javalab.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // URL 경로와 파일 시스템 경로를 매핑
        registry.addResourceHandler("/jobPost/logo/**")
                .addResourceLocations("file:/C:/filetest/upload/");
    }
}
