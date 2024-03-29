# Backend

---

### API 로직 진행 소개

---

##### `@PostMapping("/api/auth/join)` : 회원가입

- UserRepository 에서 (unique = true) 속성을 부여한 id 값이 존재하는지 조회
- 문제 없으면 `password.encode() & 권한 부여` 데이터를 추가하여 userEntity 를 `save`
- 공통응답 DTO 로 응답처리, `CMResponse.(success, error)Response(msg = "", data = "")`

---

##### `@PostMapping("/api/auth/login)` : 로그인

- 로그인 요청 시 : `attemptAuthentication()` 메서드 실행
  - 시큐리티 로그인은 `AuthenticationFilter`의 `AuthenticationManager`를 통해 인증을 처리한다. `(UsernamePasswordAuthenticationFilter) 사용`
    - Servlet 요청 객체인 (HttpServletRequest) 에서 `username & password`를 기반으로 인증 객체인 `UsernamePasswordAuthenticationToken` 을 생성
    - `AuthenticationFilter`는 `AuthenticationManager`에게 인증 객체를 전달
    - `AuthenticationManager`는 전달받은 인증 객체의 정보를 `UserDetailsService`로 전달
    - `UserDetailsService`는 전달 받은 사용자 정보를 통해 DB에서 알맞는 사용자를 찾고 이를 기반으로 `UserDetails`객체를 생성
    - 생성된 `UserDetails`객체의 인증에 성공하면 `권한(authorities)`이 담긴 검증된 인증 객체를 다시 `AuthenticationManager`로 전달
    - `AuthenticationManager`는 `AuthenticationFilter`에게 인증 객체를 다시 전달
    - `AuthenticationFilter는` 검증된 인증 객체를 `SecurityContextHolder의` `SecurityContext에` 저장
  - 기본적으로 `("/login")` 요청 시 시큐리티가 해당 요청을 가로채서 인증작업을 진행한다.
    - `setFilterProcessesUrl("/api/auth/login")` 를 사용하여 요청 url을 변경
- 로그인 성공 시 : `successfulAuthentication()` 메서드 실행
  - `accessToken & refreshToken`을 각각 생성 해서 값을 전달
    - `accessToken`은 성공응답의 body 에 깂을 담아 함께 전송
    - `refreshToken`은 redis 저장 및 쿠키로 생성하여 헤더에 함께 담아 전송
- 로그인 실패 시 : `unsuccessfulAuthentication()` 메서드 실행
  - 기본으로 `OK` 상태코드가 날라가기 때문에 `UNAUTHORIZED` 상태코드를 설정
  - ObjectMapper 를 사용하여 실패에 대한 공통적인 응답을 처리
- 공통응답 DTO 로 응답처리, `CMResponse.(success, error)Response(msg = "", data = "")`

---

##### `@PostMapping("/api/auth/logout)` : 로그아웃

- `redis` 에 저장된 토큰정보 삭제
  - `@RequestHeader("Authorization")` 설정된 `accessToken` 정보가 넘어오면 `Decode`를 통해 정보가 유효한지를 체크
  - 토큰 생성시 작성하였던 `withClaim 값인 "username"` 값을 기반으로 RefreshTokenRepository 에서 refreshToken 의 Entity 를 조회
  - 조회되는 refreshTokenEntity 정보를 삭제
- 공통응답 전송시에 헤더에 `빈값의 쿠키를 생성`하여 함께 전송한다.
  - 쿠키명은 같지만, 값은 설정하지 않은 쿠키를 생성
  - 생성시 `max-age(만료 기간)` 옵션의 값을 `"0"` 으로 지정하여 기존 `REFRESH_COOKIE` 를 지우도록 설정.
- 공통응답 DTO 로 응답처리, `CMResponse.(success, error)Response(msg = "", data = "")`

---

##### `@PostMapping("/api/auth/reissue)` : 토큰재발행

- `refreshToken` 재발급
  - `@CookieValue(value = "refresh-token", required = false)` 설정된 쿠키값을 수신
  - 쿠키명이 `"refresh-token"` 으로 넘어온 토큰 값이 유효한지를 우선 검증 필요
  - 토큰의 페이로드(Payload) 값을 기준으로, `redis` 에 저장되어 있는 refreshToken Entity 정보를 조회
  - 토큰의 무결성이 확인되고 관리중인 Redis 정보와 일치하면 `accessToken & refreshToken`을 각각 생성 해서 값을 전송
- `accessToken` 재발급
  - 새로운 `accessToken` 을 생성하여 body 값에 담아 전송
  - `refreshToken`은 redis 저장 및 쿠키로 생성하여 헤더에 함께 담아 전송
- 공통응답 DTO 로 응답처리, `CMResponse.(success, error)Response(msg = "", data = "")`

---

# 정리중...
