package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.domain.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expirationAt}")
    private long expirationAt;

    @Value("${jwt.secretKeyAt}")
    private String secretKeyAt;

    private Key secret_at_key;
//    스피링빈잉 만들어지는 시점에 빈이 만들어진 직후에 아래 메서드가 바로 실행
    @PostConstruct
    public void init(){
        secret_at_key = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyAt), SignatureAlgorithm.HS256.getJcaName());
    }
    public String createAtToken(Author author){
        String email = author.getEmail();
        String role = author.getRole().toString();
//        claims는 페이로드(사용자정보)
        Claims claims = Jwts.claims().setSubject(email);
//        주된키값을 제외한 나머지 사용자정보는 put사용하여 key:value세팅
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationAt*60*1000))  //30분을 세팅(밀리초단위)
//                secret 키를 통해 signiture 생성
                .signWith(secret_at_key)
                .compact();
        return token;
    }
}
