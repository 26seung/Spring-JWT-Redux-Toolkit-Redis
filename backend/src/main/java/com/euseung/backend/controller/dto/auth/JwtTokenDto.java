package com.euseung.backend.controller.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {

    private String accessToken;
    private String expireTime;

}
