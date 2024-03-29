package com.euseung.backend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.euseung.backend.domain.User;
import com.euseung.backend.repository.UserRepository;
import com.euseung.backend.security.auth.PrincipalDetails;
import com.euseung.backend.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;


    //  인증이나 권한이 필요한 주소요청이 있을 시 해당 필터 매서드를 타게 된다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println(":::::::::::::::::::::::::::::::::::::::::::: [doFilterInternal]");
        String jwtToken = getJwtHeader(request);
        //  accessToken 값이 넘어왔는지 체크 & 정상적인 사용자인지를 확인
        if (jwtToken != null && jwtTokenUtils.validateToken(jwtToken)){
            //  accessToken 값의 서명을 확인하여 username 값을 조회
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();
            //  서명이 정상적으로 되면 userEntity 정보를 조회
            if (username != null) {
                User userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                        new UsernameNotFoundException("해당 아이디 : " + username + " 는 없는 사용자입니다."));

                //  JWT 토큰 서명을 통해서 서명이 정상이면 "Authentication" 객체를 생성.
                //  여기서는 이미 인증되었고 강제로 토큰을 생성하는 것이기 때문에 PW는 null 해주어도 상관 X
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails,
                        null,
                        principalDetails.getAuthorities());

                //  시큐리티에서 사용하는 SecurityContext 공간에 "Authentication" 객체를 저장.
                //  @AuthenticationPrincipal 을 사용하여 SecurityContext 에 저장된 정보를 조회
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        System.out.println("::::::::::::::::::::::::::::::::::::::::::::");
        filterChain.doFilter(request, response);
    }

    //  해더값을 확인하여 토큰이 존재한다면, "Bearer " 를 제거한 토큰 값을 리턴 / 없다면 그냥 Null 값을 리턴
    private String getJwtHeader(HttpServletRequest request){

        String bearerToken = request.getHeader(JwtProperties.HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.TOKEN_PREFIX)){
            return bearerToken.replace(JwtProperties.TOKEN_PREFIX,"");
        }
        return null;
    }



}
