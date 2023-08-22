package com.euseung.backend.handler;

import com.euseung.backend.controller.dto.CMResponse;
import com.euseung.backend.handler.ex.AccessTokenNotValidException;
import com.euseung.backend.handler.ex.CustomValidationException;
import com.euseung.backend.handler.ex.RefreshTokenValidationException;
import com.euseung.backend.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final CookieUtils cookieUtils;

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationException(CustomValidationException e){
        log.info("[GlobalExceptionHandler] - validationException : {} ", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CMResponse.errorResponse(e.getMessage(), e.getErrorMap()));
    }


    //  token Exception
    @ExceptionHandler(AccessTokenNotValidException.class)
    public ResponseEntity<?> accessTokenNotValidException(AccessTokenNotValidException e){
        log.info("[GlobalExceptionHandler] - accessTokenNotValidException : {} ", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CMResponse.errorResponse(e.getMessage(), null));
    }
    @ExceptionHandler(RefreshTokenValidationException.class)
    public ResponseEntity<?> RefreshTokenValidationException(RefreshTokenValidationException e){
        // 쿠키 삭제
        ResponseCookie responseCookie = cookieUtils.removeRefreshTokenCookie();
        log.info("[GlobalExceptionHandler] - RefreshTokenValidationException : {} ", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(CMResponse.errorResponse(e.getMessage(), null));
    }
}
