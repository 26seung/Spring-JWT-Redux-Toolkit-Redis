# Backend

---

### 간단한 로직 흐름


#### `@PostMapping("/api/auth/join)` : 회원가입

- UserRepository 에서 (unique = true) 속성을 부여한 id 값이 존재하는지 조회
- 문제 없으면 `password.encode() & 권한 부여` 데이터를 추가하여 userEntity 를 `save`
- 공통응답 DTO 로 응답처리, `CMResponse.(success, error)Response(msg = "", data = "")`


#### `@PostMapping("/api/auth/login)` : 로그인

- 로그인 요청 시 : `attemptAuthentication()` 메서드 실행
  - 시큐리티 로그인은 `AuthenticationFilter`의 `AuthenticationManager`를 통 인증을 처리한다. `(UsernamePasswordAuthenticationFilter)`
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


#### `@PostMapping("/api/auth/logout)` : 로그아웃

- `redis` 에 저장된 토큰정보 삭제
  - `@RequestHeader("Authorization")` 설정된 `accessToken` 정보가 넘어오면 `Decode`를 통해 정보가 유효한지를 체크
  - 토큰 생성시 작성하였던 `withClaim 값인 "username"` 값을 기반으로 RefreshTokenRepository 에서 refreshToken 의 Entity 를 조회
  - 조회되는 refreshTokenEntity 정보를 삭제
- 공통응답 전송시에 헤더에 `빈값의 쿠키를 생성`하여 함께 전송한다.
  - 쿠키명은 같지만, 값은 설정하지 않은 쿠키를 생성
  - 생성시 `max-age(만료 기간)` 옵션의 값을 `"0"` 으로 지정하여 기존 `REFRESH_COOKIE` 를 지우도록 설정.
- 공통응답 DTO 로 응답처리, `CMResponse.(success, error)Response(msg = "", data = "")`


#### `@PostMapping("/api/auth/reissue)` : 토큰재발행

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

# 정리중...

#### SecurityConfig

httpBasic() 이란 headers 에다 Authorization 이라는 Key 에 (Id, Pw) 값을 넣어 요청하는 방식이다.
중간에 id,pw 가 노출이 되는 위험이 있기 때문에 https 방식을 사용하여 암호화하여 사용해야한다.

httpBasic 방식은 (id,pw)를 사용하여 탈취에 대한 위험이 있기 때문에 상대적으로 안전한 토큰을 사용하여 이동시키는 Bearer 방식이 존재한다.

---

#### 시큐리티 로직 흐름

1. `SecurityConfig` 클래스에서 `@EnableWebSecurity` 주석을 통해 시큐리티를 활성화 한다.
2. 시큐리티에서 사용하는 속성인 `UserDetails` 을 상속받아 유저객체를 사용한다.
   - 상속받아 사용시 장점은 OAuth 인증시에도 별도 구축 필요가 없다.
3. `SecurityConfig` 클래스에서 페이지에 대한 권한처리 와 필터처리를 해줄수 있다.
   - 여기서 설정하는 필터는 기존 필터들보다 우선해서 실행된다.
4. 시큐리티에서 제공하는 `UsernamePasswordAuthenticationFilter` 를 통해서 (ID,PW) 확인하여 로그인을 진행
   - `AuthenticationManager` 를 받아와서 사용가능
   - 로그인 시도 시 `attemptAuthentication()` 함수를 실행한다.
   - 정상적이면 `UserDetailsService` 를 상속한 `PrincipalDetailsService` 클래스의 `loadUserByUsername()` 함수가 실행된다.
   - `UserDetails` 을 세션에 담고 JWT 토큰을 생성하여 응답해주면 된다.
     - 세션에 담는 이유는 권한관리를 하기 위해서...
   - `UsernamePasswordAuthenticationToken` 에 username 와 password 를 담아서 `authenticationManager`를 실행하면 `loadUserByUsername()` 함수가 실행된다.
   - `Authentication` 을 담은 `authentication` 을 리턴 해주면 된다.
     - 리턴시 `authentication` 객체가 세션 영역에 저장된다.
     - `PrincipalDetailsService` 클래스의 `loadUserByUsername()` 함수가 실행후 정상이면 `authentication` 객체가 리턴됨
   - `attemptAuthentication()` 함수가 종료되면 `successfulAuthentication()` 함수가 실행된다.
     - JWT 토큰 사용시에는 토큰을 생성하여 request 요청한 사용자에게 JWT 토큰을 response 해주면 된다.
   - `UsernamePasswordAuthenticationFilter` 의 `attemptAuthentication()` 함수는 `'/login'` 주소로의 요청시에만 실행되기에 api 실행시 별도 변경처리를 해주어야 한다.
     - `setFilterProcessesUrl("/api/auth/login");` 를 사용하여 요청 url을 변경해주었다.

- JWT 사용을 위해서 build 추가 필요 `implementation group: 'com.auth0', name: 'java-jwt', version: '4.2.1'` 빌드
- 유저 정보가 확인되면 빌더패턴을 사용한 토큰을 생성 (해쉬형태로 빌더)

---

##### Optional 사용하여 Null 예외 처리

`UserRepository` 클래스 에서

```
public User findByUsername(String username);
이 아닌
Optional<User> findByUsername(String username);
```

- Optional 을 사용하는 이유는 Null 예외처리 발생 때문이다.
- public 으로 코드 생성하여 동작시 조회되지 않는 아이디로 로그인 시도시 `InternalAuthenticationServiceException` 오류가 발생하지만

```
User userEntity = userRepository.findByUsername(username).orElseThrow(()->
        new UsernameNotFoundException("User Not Found with username: " + username));
```

(Optional) 처리 시 예외오류가 발생하지 않도록 할 수 있다..

---

### 쿠키사용

1. 쿠키의 저장을 위해서는 클라이언트와 서버 모두 `Credentials` 부분을 `true`로 설정이 필요
   - 클라이언트와 http://localhost:3000 서버가 http://localhost:8080 서로 같은 Host이고 Port만 다른 셈이다.
   - 기본적으로 브라우저가 제공하는 요청 API 들은 별도의 옵션 없이 브라우저의 쿠키와 같은 인증과 관련된 데이터를 함부로 요청 데이터에 담지 않도록 되어있다. 이는 응답을 받을때도 마찬가지이다.
   - 프론트에서 axios 요청할 때, `withCredentials` 부분을 `true`로 해서 수동으로 CORS 요청에 쿠키값을 넣어줘야 한다.
2. HttpOnly 설정 사용시 자바스크립트 `document.cookie` 같은 문법으로 접근이 불가능하다.
   - 보통의 쿠키만 콘솔창에 검색이 되는 모습을 볼 수 있다.
3. (의문) 크롬 옵션중에 (타사 쿠키 차단) 이 있는데, 해당 옵션 사용시 토큰 쿠키저장이 불가능 하였다. 그러면 로그인 수행이 불가능한지???
   - 재부팅후 수행시 쿠키 저장이 되네?

<img width="825" alt="image" src="https://user-images.githubusercontent.com/79305451/220356788-82752263-d884-4d49-8ad3-4c40a1871547.png">

---

#### Redis 사용

- cli 콘솔을 이용하여 key 조회시 데이터 img

  - redisTemplate 사용 시

    - <img width="179" alt="image" src="https://user-images.githubusercontent.com/79305451/226169710-022d5282-268c-4173-9296-c711090b8f48.png">

  - redisRepository 사용 시
    - <img width="516" alt="image" src="https://user-images.githubusercontent.com/79305451/226169837-bfc9bbc8-7a09-42d0-9798-5fb90d6fb2db.png">

---

| redisTemplate 사용 img                                                                                                                    | redisRepository 사용 img                                                                                                                  |
| ----------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| <img width="150" alt="image" src="https://user-images.githubusercontent.com/79305451/226169710-022d5282-268c-4173-9296-c711090b8f48.png"> | <img width="300" alt="image" src="https://user-images.githubusercontent.com/79305451/226169837-bfc9bbc8-7a09-42d0-9798-5fb90d6fb2db.png"> |

---

UsernamePasswordAuthenticationToken [Principal=test1, Credentials=[PROTECTED], Authenticated=false, Details=null, Granted Authorities=[]]
UsernamePasswordAuthenticationToken [Principal=PrincipalDetails(user=User(id=2, username=test1, password=$2a$10$MgJEUK/zjMSjJj0s4sTvIugsca/SVZRO3Wrn91Zc50kG2HUsvl/Gm, email=null, eRole=ROLE_USER, createDate=2023-08-12T19:55:13.354582)), Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[com.euseung.backend.security.auth.PrincipalDetails$$Lambda$1416/0x0000000800b9fc40@4a911dc3]]
