package com.euseung.backend.security.jwt;

import com.euseung.backend.controller.dto.CMResponse;
import com.euseung.backend.controller.dto.auth.JwtTokenDto;
import com.euseung.backend.controller.dto.auth.SaveTokenDto;
import com.euseung.backend.controller.dto.auth.UserRequestDto;
import com.euseung.backend.domain.User;
import com.euseung.backend.repository.UserRepository;
import com.euseung.backend.security.auth.PrincipalDetails;
import com.euseung.backend.service.TokenService;
import com.euseung.backend.utils.CookieUtils;
import com.euseung.backend.utils.IpAddressUtil;
import com.euseung.backend.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final CookieUtils cookieUtils;
    private final TokenService tokenService;
    private final UserRepository userRepository;


    //  로그인 요청시 attemptAuthentication() 실행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserRequestDto.SignIn signIn = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.SignIn.class);

            //  유저네임패스워드 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            signIn.getUsername(),
                            signIn.getPassword()
                    );
            // [PrincipalDetailsService]-loadUserByUsername() 함수가 실행, JPA 에서 username 의 정보를 확인
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //  attemptAuthentication 싫행 후 요청이 정상적으로 진행되면 successfulAuthentication 함수가 실행된다.
    //  JWT 토큰을 생성하여 request 요청한 사용자에게 JWT 토큰을 response 해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        // 가져온 authentication 객체에서 (PrincipalDetails) 다운캐스팅
        String getUsername = ((PrincipalDetails) authentication.getPrincipal()).getUsername();
        String getClientIp = ((PrincipalDetails) authentication.getPrincipal()).getUser().getIpAddress();
        String currentClientIp = IpAddressUtil.getClientIp(request);

        //  ==========================================================================
        //  최초 로그인한 ip 와 같은지 확인 (처리 방식에 따라 로그인을 하지 않거나 메일 등의 알림을 주는 방법을 구현)
        if(getClientIp != currentClientIp){
            log.warn("The last access location {} and the current access location are different from the record." + getClientIp);
            User userEntity = userRepository.findByUsername(getUsername).orElseThrow(()->
                    new UsernameNotFoundException("User Not Found with username : " + getUsername));
            userEntity.setIpAddress(currentClientIp);
            userRepository.save(userEntity);
        }
        //  ==========================================================================

        //  accessToken 생성
        String accessToken = jwtTokenUtils.generateAccessToken(getUsername);
        Date expiredTime = jwtTokenUtils.getExpiredTime(accessToken);

        //  refreshToken 생성
        String refreshToken = jwtTokenUtils.generateRefreshToken();
        //  refreshToken 의 Cookie 값을 생성
        ResponseCookie responseCookie = cookieUtils.generateRefreshTokenCookie(refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        //  refreshToken 을 DB 에 저장 (username & uuid)
        SaveTokenDto saveTokenDto = SaveTokenDto.builder()
                .username(jwtTokenUtils.getUserId(accessToken))
                .uuid(jwtTokenUtils.getRefreshTokenId(refreshToken))
                .build();

        tokenService.새로고침토큰저장(saveTokenDto);

        //  =================================================================
        //  데이터를 JSON 형태로 전송.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //  Map 을 사용하여 body 값 설정.
        //  Java 9 이상부터 Map.of() 를 사용하여 간단하게 Map 을 초기화하여 사용할 수 있다.
        //  put(key, value) 형식을 사용하지 않고 그냥 key, value 형식으로 사용 가능. (# 다만 해당 인자는 10개 까지만 사용 가능, 11개는 오류)
        Map<String,Object> responseBody = Map.of(
                "accessToken", accessToken,
                "expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime)
        );

        //  DTO 를 통해 공통된 응답으로 accessToken 데이터 전송
        new ObjectMapper().writeValue(response.getOutputStream(), CMResponse.successResponse("accessToken issue", responseBody));
    }


    //  로그인 시도 실패시 해당 로직 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.warn("unsuccessfulAuthentication failed.getLocalizedMessage() : {}", failed.getLocalizedMessage());

        //  실패 상태코드를 날려주지 않으면 성공 상태코드가 날라감.
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), CMResponse.errorResponse(failed.getMessage(),null));
    }
}
