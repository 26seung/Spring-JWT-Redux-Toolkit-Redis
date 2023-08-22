package com.euseung.backend.service;

import com.euseung.backend.controller.dto.auth.UserRequestDto;

public interface AuthService {
    void 회원가입(UserRequestDto.SignUp signUp);
}
