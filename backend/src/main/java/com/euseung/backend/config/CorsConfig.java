package com.euseung.backend.config;

import com.euseung.backend.security.jwt.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        //  기본적으로 CORS 는 허용된 헤더만 노출되므로 "authorization" 경우 별도 설정이 필요
        config.addExposedHeader(JwtProperties.HEADER_STRING);

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
