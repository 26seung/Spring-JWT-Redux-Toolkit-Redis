package com.euseung.backend.security.jwt;

public interface JwtProperties {

    //  token 들의 만료시간
    Long ACCESS_EXPIRATION_TIME = 1000 * 60 * 60L; // 1000 * 60 * 60 = 1시간 , 864000000 = 10일 (1/1000초)
    Long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 30L ;    //  [ms 단위]  (30 Day)
    Long REDIS_REFRESH_EXPIRATION_TIME = 60 * 60 * 24 * 30L;       //  [초 단위]  (30 Day)

    //  token 암호 값 & 네이밍
    String SECRET = "Euseung"; // 우리 서버만 알고 있는 비밀값
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String REFRESH_COOKIE_NAME = "refresh-token";

}
