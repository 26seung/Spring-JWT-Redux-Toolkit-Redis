package com.euseung.backend.security.auth;

import com.euseung.backend.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*
 .시큐리티가 "/login" 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
 .로그인 진행이 완료가 되면 시큐리티 session 을 만들어준다 (Security ContextHolder)
 .오브젝트 타입 => Authentication 타입 객체
 .Authentication 안에 User 정보가 있어야 됨
 .User 오브젝트 타입 => UserDetails 타입 객체

 Security Session => Authentication => UserDetails
*/

@Data
public class PrincipalDetails implements UserDetails {

    private User user;
    public PrincipalDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(()-> user.getERole().toString());
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
