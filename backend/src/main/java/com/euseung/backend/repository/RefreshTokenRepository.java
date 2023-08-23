package com.euseung.backend.repository;

import com.euseung.backend.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//  repository 의 save 사용시 hash 형태로 값이 저장된다.
//  hash 형태의 값을 확인하기 위해서는 hkeys : 저장된 key 값, hvals : 저장된 value 값, hgetall : 저장된 key, value 값
//  ex : hvals refresh_token:test ,  hgetall refresh_token:test
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshTokenId(String refreshTokenId);

}
