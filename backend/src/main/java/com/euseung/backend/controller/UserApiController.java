package com.euseung.backend.controller;

import com.euseung.backend.security.auth.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
@Slf4j
public class UserApiController {

    //  @AuthenticationPrincipal 은 SecurityContext 에 저장된 정보를 조회..
    @GetMapping("/guest")
    public String guest(@AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("All visitors can access the guest page. : {}" , principalDetails);
        return "guest page";
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("{(ROLE_USER) AND (ROLE_ADMIN)} visitors can access the user page. : {}", principalDetails);
        return "user page";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("only (ROLE_ADMIN) visitors can access the admin page. : {}", principalDetails);
        return "admin page";
    }
}
