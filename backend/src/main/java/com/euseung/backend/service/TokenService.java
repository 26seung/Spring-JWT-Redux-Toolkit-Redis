package com.euseung.backend.service;

import com.euseung.backend.controller.dto.auth.JwtTokenDto;
import com.euseung.backend.controller.dto.auth.SaveTokenDto;

public interface TokenService {

    void 로그아웃토큰(String accessToken);
    //  유저의 아이디 , refreshToken 생성시 uuid
    void 새로고침토큰저장(SaveTokenDto saveTokenDto);
    JwtTokenDto 토큰재발급(String refreshToken);

}
