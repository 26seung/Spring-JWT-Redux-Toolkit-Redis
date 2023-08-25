package com.euseung.backend.security.auth;

import com.euseung.backend.domain.User;
import com.euseung.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    // 시큐리티 설정에서 loginProcessingUrl("/login"); 걸었기 때문에
    // 로그인 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어있는 `loadUserByUsername` 함수가 실행된다.
    // 시큐리티 Session (내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User Not Found with username : " + username));

        return new PrincipalDetails(userEntity);
    }
}
