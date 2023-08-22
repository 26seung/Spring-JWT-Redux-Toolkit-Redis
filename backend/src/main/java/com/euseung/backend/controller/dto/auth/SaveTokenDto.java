package com.euseung.backend.controller.dto.auth;

import lombok.Data;

import lombok.Builder;

@Data
@Builder
public class SaveTokenDto {

    private String username;
    private String uuid;
}
