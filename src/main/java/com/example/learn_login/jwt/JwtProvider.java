package com.example.learn_login.jwt;

import com.example.learn_login.entity.RefreshRepo;
import com.example.learn_login.entity.RefreshToken;
import com.example.learn_login.exception.ForbiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

    public UsernamePasswordAuthenticationToken generateAuthentication(String token) {
        Claims claim = tokenParser(token);
        return new UsernamePasswordAuthenticationToken(claim.getSubject(), "");
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

    public Claims tokenParser(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Boolean isNonExpired(String token) {
        return !tokenParser(token).getExpiration().after(new Date());
    }

    public String parseRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(!token.startsWith("Bearer")) {
            throw new ForbiddenException();
        }
        return token.substring(7);
    }

}
