package com.euseung.backend.service.impl;


import com.euseung.backend.controller.dto.auth.UserRequestDto;
import com.euseung.backend.domain.ERole;
import com.euseung.backend.domain.User;
import com.euseung.backend.handler.ex.CustomValidationException;
import com.euseung.backend.repository.UserRepository;
import com.euseung.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void 회원가입(UserRequestDto.SignUp signUp) {

        //  @Column(unique = true) 은 BindingResult 에서 캐치 하지 못하므로 AOP 발동되지 않음
        if (userRepository.existsByUsername(signUp.getUsername())) {
            throw new CustomValidationException("중복된 아이디 (" + signUp.getUsername() + ") 가 존재합니다.");
        }
        //  새로운 userEntity 정보를 DB에 저장
        User userEntity = User.builder()
                .username(signUp.getUsername())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .eRole(ERole.ROLE_USER)
                .build();

        userRepository.save(userEntity);
    }
}