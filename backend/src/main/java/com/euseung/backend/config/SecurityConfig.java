package com.euseung.backend.config;

import com.euseung.backend.handler.jwt.AccessDeniedHandlerImpl;
import com.euseung.backend.handler.jwt.AuthenticationEntryPointImpl;
import com.euseung.backend.repository.UserRepository;
import com.euseung.backend.security.jwt.JwtAuthenticationFilter;
import com.euseung.backend.security.jwt.JwtAuthorizationFilter;
import com.euseung.backend.service.TokenService;
import com.euseung.backend.utils.CookieUtils;
import com.euseung.backend.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final AuthenticationEntryPointImpl unauthorizedEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final JwtTokenUtils jwtTokenUtils;
    private final CookieUtils cookieUtils;
    private final TokenService tokenService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //  httpBasic, csrf, formLogin, session disable
        http
                .csrf().disable()
                .formLogin().disable()      //  formLogin 대신 Jwt를 사용하기 때문에 disable로 설정
                .httpBasic().disable()      //  httpBasic 방식 대신 Jwt를 사용하기 때문에 disable로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);     //  Jwt를 사용하기 때문에 session을 stateless로 설정한다. stateless로 설정 시 Spring Security는 세션을 사용하지 않는다.

        //  요청에 대한 권한 설정
        http.authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/user/**").permitAll()
                .anyRequest().authenticated();

        //  jwt filter 설정
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(unauthorizedEntryPoint)
                .and()
                .apply(new CustomFilter());

        return http.build();
    }

    public class CustomFilter extends AbstractHttpConfigurer<CustomFilter,HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenUtils, cookieUtils, tokenService, userRepository);
            //  로그인 경로를 임의 설정 (기존 : "/login")
            jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

            //  jwt filter 설정
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterBefore(new JwtAuthorizationFilter(authenticationManager, userRepository, jwtTokenUtils),
                            UsernamePasswordAuthenticationFilter.class);
        }
    }
}
