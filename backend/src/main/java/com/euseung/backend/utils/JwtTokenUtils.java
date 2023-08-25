package com.euseung.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.euseung.backend.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    //  AccessToken 생성
    public String generateAccessToken(String username){

        Date expireTime = new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(expireTime)
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    //  RefreshToken 생성
    public String generateRefreshToken(){

        Date expireTime = new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_EXPIRATION_TIME);
        String refreshTokenId = String.valueOf(UUID.randomUUID());

        return JWT.create()
                .withSubject(refreshTokenId)
                .withIssuedAt(new Date())
                .withExpiresAt(expireTime)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    //  withSubject 에 넣어주었던 username 아이디 값을 반환.
    public String getUserId(String token){
        return JWT.decode(token).getSubject();
    }
    //  토큰 생성시 설정된 withExpiresAt 만료시간 값을 반환.
    public Date getExpiredTime(String token){
        return JWT.decode(token).getExpiresAt();
    }
    //  refreshToken 생성시 넣어둔 UUID 값을 반환.
    public String getRefreshTokenId(String token) {
        return JWT.decode(token).getSubject();
    }

    //  토큰 유효성 검사
    public boolean validateToken(String token){
        try {
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);

            //  decode 옵션을 통해 jwt 토큰의 객체 정보를 확인
//            DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
            log.info("=================== test_verifyJwtToken ===================");
//            log.info("jwt token         : " + decodedJwt.getToken());
//            log.info("jwt algorithm     : " + decodedJwt.getAlgorithm());
//            log.info("jwt claims        : " + decodedJwt.getClaims());
//            log.info("jwt issuer        : " + decodedJwt.getIssuer());
//            log.info("jwt issuer date   : " + decodedJwt.getIssuedAt());
//            log.info("jwt expires date  : " + decodedJwt.getExpiresAt());
//            log.info("jwt signature     : " + decodedJwt.getSignature());
//            log.info("jwt subject       : " + decodedJwt.getSubject());
//            log.info("jwt payload       : " + decodedJwt.getPayload());
//            log.info("============================================");
            return true;

        }catch (JWTVerificationException e){
            log.error("JWTVerificationException : {}", e.getMessage());
        }
        return false;
    }
}
