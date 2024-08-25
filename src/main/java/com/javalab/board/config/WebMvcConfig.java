package com.javalab.board.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    
    //파일 업로드시 바로 보이게
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본적인 정적 리소스 경로 설정
        registry.addResourceHandler("/**")
                .addResourceLocations("file:src/main/resources/static/");

        registry.addResourceHandler("/jobPost/logo/**")
                .addResourceLocations("file:/C:/filetest/upload/");

        registry.addResourceHandler("/upload/**")
                        .addResourceLocations("file:/C:/filetest/upload/");
        // 이미지 업로드 경로 설정
        registry.addResourceHandler("/jobPost/uploaded/**")
                .addResourceLocations("file:C:/filetest/upload/");

        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:C:/filetest/upload/");

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9000") // 허용할 출처를 명시
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 자격 증명 허용
    }

}

