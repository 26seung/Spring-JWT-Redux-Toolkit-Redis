package com.euseung.backend.controller.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
public class UserRequestDto {


    @Getter
    @Setter
    public static class SignUp {

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        @Size(min = 2,max = 50)
        private String username;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
//        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;
    }

    @Getter
    @Setter
    public static class SignIn {
        @NotBlank(message = "아이디는 필수 입력값입니다.")
        @Size(min = 2,max = 50)
        private String username;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;
    }

}
