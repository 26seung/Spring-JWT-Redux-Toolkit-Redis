package com.euseung.backend.service.impl;

import com.euseung.backend.controller.dto.auth.JwtTokenDto;
import com.euseung.backend.controller.dto.auth.SaveTokenDto;
import com.euseung.backend.domain.RefreshToken;
import com.euseung.backend.handler.ex.AccessTokenNotValidException;
import com.euseung.backend.handler.ex.RefreshTokenValidationException;
import com.euseung.backend.repository.RefreshTokenRepository;
import com.euseung.backend.repository.UserRepository;
import com.euseung.backend.security.jwt.JwtProperties;
import com.euseung.backend.service.TokenService;
import com.euseung.backend.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void 로그아웃토큰(String accessToken){
        //  토큰 헤더 Bearer 제거
        String logoutAccessToken = getAccessToken(accessToken);
        //  accessToken 의 유효성 검증
        if (!jwtTokenUtils.validateToken(logoutAccessToken)){
            throw new AccessTokenNotValidException("access token is not valid.");
        }
        //  accessToken 의 username 값 으로 refreshToken 의 Entity 조회
        RefreshToken refreshToken = refreshTokenRepository.findById(jwtTokenUtils.getUserId(logoutAccessToken))
                .orElseThrow(()-> new RefreshTokenValidationException("refreshToken does not exist in Redis server"));
        //  refreshTokenEntity 정보를 삭제
        refreshTokenRepository.delete(refreshToken);
}

    //  정상적인 사용자라면 토큰정보를 Redis 저장
    @Override
    public void 새로고침토큰저장(SaveTokenDto saveTokenDto) {
        //  User Entity 정보에서 username 저장 여부를 확인
        if (!userRepository.findByUsername(saveTokenDto.getUsername()).isPresent()){
            throw new UsernameNotFoundException("해당 아이디 : " + saveTokenDto.getUsername() + " 는 없는 사용자입니다.");
        }
        //  username = 유저Id , uuid = 리프레시토큰Id
        refreshTokenRepository.save(new RefreshToken(
                saveTokenDto.getUsername(),
                saveTokenDto.getUuid())
        );
    }

    //  1. accessToken 이 만료된 경우 새로운 토큰이 발급되어야 한다.
    //  2. request 로 전달 받은 refreshToken 에 대한 (redis 정보와 유효성 검사)를 진행하고 유효하지 않다면 `401 에러`를 반환한다.
    //  3. 새로 발급된 accessToken 은 responseBody 에, refreshToken 의 경우 cookie 값에 넣어 전달한다.
    @Override
    public JwtTokenDto 토큰재발급(String refreshToken) {
        //  cookie 전송된 암호화 refreshToken 의 refreshTokenId (uuid)
        String refreshTokenId = jwtTokenUtils.getRefreshTokenId(refreshToken);

        //  redis 저장된 refreshToken Entity 확인, 전송받은 cookie 값으로 비교
        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshTokenId(refreshTokenId)
                .orElseThrow(()-> new RefreshTokenValidationException("redis 에 등록된 해당 유저 (" + refreshTokenId + ")를 찾을 수 없습니다."));

        //  저장된 refreshToken 정보를 검증..
        if (!jwtTokenUtils.validateToken(refreshToken)){
            //  유효하지 않으면 refreshToken 삭제
            refreshTokenRepository.delete(findRefreshToken);
            throw new RefreshTokenValidationException(refreshToken, "Not a valid refreshToken value.");
        }

        //  저장된 refreshToken 의 username
        String tokenUsername = findRefreshToken.getUsername();
        //  새로운 refreshToken 재발급
        SaveTokenDto saveTokenDto = SaveTokenDto.builder()
                .username(tokenUsername)
                .uuid(refreshTokenId)
                .build();

        새로고침토큰저장(saveTokenDto);

        //  새로운 accessToken 재발급
        String newAccessToken = jwtTokenUtils.generateAccessToken(findRefreshToken.getUsername());
        Date expiredTime = jwtTokenUtils.getExpiredTime(newAccessToken);

        return JwtTokenDto.builder()
                .accessToken(newAccessToken)
                .expireTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime))
                .build();
    }


    //  넘어오는 accessToken 헤더값 Bearer 제거
    private String getAccessToken(String accessToken){
        return accessToken.replace(JwtProperties.TOKEN_PREFIX,"");
    }
}
