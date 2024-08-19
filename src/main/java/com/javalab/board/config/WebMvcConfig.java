package com.javalab.board.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    
    //파일 업로드시 바로 보이게
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:src/main/resources/static/");

    }

    // URL 경로와 파일 시스템 경로를 매핑
    public void addResourceHandlers2(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/jobPost/logo/**")
            .addResourceLocations("file:/C:/filetest/upload/");
        }
}
