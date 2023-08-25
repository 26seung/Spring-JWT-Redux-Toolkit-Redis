package com.euseung.backend.utils;

import com.euseung.backend.security.jwt.JwtProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {


    //  httpOnly : XSS 공격 방지.
    //  가장 대표적인 공격중 하나가 XSS (Cross Site Scripting) 게시판 제목 또는 이미지 'src' 에 해커사이트 주소로 연결되게 작성하면 쿠키를 탈취할 수 있게 된다
    //  이러한 스크립트를 통한 탈취방법을 막고자 브라우저에서는 쿠키에 접근할 수 없도록 제한하기 위한 옵션이 HTTP Only 이다.

    //  secure : (HTTP / HTTPS) 통신을 구분하여 보안강화.
    //  secure 라는 접미사를 사용하여 쿠키를 생성하면, 브라우저는 'HTTPS' 가 아닌 통신에서는 쿠키를 전송하지 않는다. (배포시 True)
    public ResponseCookie generateRefreshTokenCookie(String refreshToken){
        return ResponseCookie.from(JwtProperties.REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtProperties.REFRESH_EXPIRATION_TIME)
                .build();
    }
    //  refreshCookie : 빈값의 쿠키값을 전송하여 기존 등록된 쿠키값을 제거한다.
    //  생성시 expires(유효 일자)나 max-age(만료 기간) 옵션이 지정되어있지 않으면, 브라우저가 닫힐 때 쿠키도 함께 삭제.
    //  이런 쿠키를 "세션 쿠키(session cookie)"라고 부른다.
    //  expires 나 max-age 옵션을 설정하면 브라우저를 닫아도 쿠키가 삭제되지 않고, 빈값의 쿠키를 전송하여 기존 값을 제거할 수 있다.
    public ResponseCookie removeRefreshTokenCookie() {
        return ResponseCookie.from(JwtProperties.REFRESH_COOKIE_NAME, "")
                .path("/")
                .maxAge(0)
                .build();
    }
}
