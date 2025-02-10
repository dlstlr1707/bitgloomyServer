package com.bitgloomy.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000") // React 앱 주소 (예시: localhost:3000)
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 자격 증명(Credentials) 허용
    }
}
