package com.example.learn_login.jwt;

import com.example.learn_login.entity.RefreshRepo;
import com.example.learn_login.entity.RefreshToken;
import com.example.learn_login.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String key;

    @Value("${jwt.expired.access}")
    private Long accessExp;

    @Value("${jwt.expired.refresh}")
    private Long refreshExp;

    private final RefreshRepo refreshRepo;

    public UsernamePasswordAuthenticationToken generateAuthentication(User user) {

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(user.getRole().toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new org.springframework.security.core.userdetails.User(user.getAccountId(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private String generateToken(String subject, String tokenType, Long exp) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key)
                .setExpiration(new Date(System.currentTimeMillis() + exp * 1000))
                .claim("auth", tokenType)
                .compact();
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication.getName(), "access_token", accessExp);
    }

    public String generateRefreshToken(Authentication authentication) {
        return refreshRepo.save(RefreshToken.builder()
                .key(authentication.getName())
                .value(generateToken(authentication.getName(), "refresh_token", refreshExp))
                .build()).getValueA();
    }

}
