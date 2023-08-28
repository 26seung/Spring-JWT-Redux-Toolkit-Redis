package com.euseung.backend.controller;


import com.euseung.backend.controller.dto.CMResponse;
import com.euseung.backend.controller.dto.auth.JwtTokenDto;
import com.euseung.backend.controller.dto.auth.UserRequestDto;
import com.euseung.backend.service.AuthService;
import com.euseung.backend.service.TokenService;
import com.euseung.backend.utils.CookieUtils;
import com.euseung.backend.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {


    private final AuthService authService;
    private final TokenService tokenService;
    private final CookieUtils cookieUtils;


    //  회원가입 Controller
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserRequestDto.SignUp signUp, BindingResult bindingResult){
        log.info("[AuthApiController] - join run : {}", signUp);
        //  전처리 예외처리 를 위하여 `@Valid 어노테이션과 BindingResult` 을 사용하여 SQL 문 오류를 뱉기전에 전처리 해준다..
        //  프론트 단에서도 별도의 예외처리를 진행하고, 백단에서도 예외를 처리하여 보안을 강화한다. (혹시나 허용하지 않은 API 요청을 통한 접근에 대한 문제를 막아준다.)
        authService.회원가입(signUp);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CMResponse.successResponse("SignUp Process Success",null));
    }

    //  ===========================================================================================
    //  로그인 Controller는 JwtAuthenticationFilter 클래스의 attemptAuthentication() 메서드가 자동 응답
    //  ===========================================================================================


    //  로그아웃 Controller
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken){
        log.info("[AuthApiController] - logout run : {}", accessToken);
        //  로그인 되어 있는 정보를 확인하여 DB 삭제
        //  DB 삭제 로그아웃에 필요한 비어있는 쿠키값을 가져옴 (문자열)
        tokenService.로그아웃토큰(accessToken);
        //  빈 값의 cookie 를 생성하여 기존 쿠키 값 삭제
        ResponseCookie responseCookie = cookieUtils.removeRefreshTokenCookie();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(CMResponse.successResponse("Your connection has been successfully logged out.",null));
    }

    //  token 재발급 Controller
    @PostMapping("/reissue")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh-token", required = false) String refreshToken){
        log.info("[AuthApiController] - refreshToken run : {}", refreshToken);
        //  accessToken 재생성
        JwtTokenDto jwtTokenDto = tokenService.토큰재발급(refreshToken);
        //  생성한 refreshToken 쿠키를 헤더에 담아 전송한다.
        ResponseCookie responseCookie = cookieUtils.generateRefreshTokenCookie(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(CMResponse.successResponse("Successfully regenerated (access & refresh) tokens.", jwtTokenDto));
    }
}
