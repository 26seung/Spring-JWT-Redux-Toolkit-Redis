package com.euseung.backend.handler.jwt;

import com.euseung.backend.controller.dto.CMResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//  AccessDeniedHandler 는 서버에 요청을 할 때 액세스가 가능한지 권한을 체크후 액세스 할 수 없는 요청을 했을시 동작된다.
//  AuthenticationEntryPoint 는 인증이 되지않은 유저가 요청을 했을때 동작된다.
//  허용되지 않은 권한으로 접근시 403 오류를 반환해준다..

@Component
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("[{}] AccessDeniedHandler Security error Message : {}", HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
//        response.sendError(response.SC_FORBIDDEN, accessDeniedException.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), CMResponse.errorResponse(accessDeniedException.getMessage(), null));
    }
}
