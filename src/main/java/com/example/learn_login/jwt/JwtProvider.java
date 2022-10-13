package com.example.learn_login.jwt;

import com.example.learn_login.entity.RefreshToken;
import com.example.learn_login.entity.User;
import com.example.learn_login.repository.RefreshTokenRepository;
import com.example.learn_login.repository.UserRepository;
import com.example.learn_login.exception.NotFoundException;
import com.example.learn_login.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;

    private final RefreshTokenRepository refreshRepository;

    public UsernamePasswordAuthenticationToken generateAuthentication(String token) {
        Claims claim = tokenParser(token);
        User user = userRepository.findByAccountId(claim.getSubject()).orElseThrow(NotFoundException::new);
        UserDetails principle = customUserDetailsService.loadUserByUsername(user.getAccountId());
        return new UsernamePasswordAuthenticationToken(principle, "");
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

    public String generateAccessToken(String accountId) {
        return generateToken(accountId, "access_token", accessExp);
    }

    public String generateRefreshToken(String accountId) {
        String token = generateToken(accountId, "refresh_token", refreshExp);
        setRefreshToken(accountId, token);
        return token;
    }

    public Claims tokenParser(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Boolean isNonExpired(String token) {
        return tokenParser(token).getExpiration().after(new Date());
    }

    public String parseRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return null;
    }

    private void setRefreshToken(String accountId, String token) {
        refreshRepository.save(RefreshToken.builder()
                        .accountId(accountId)
                        .value(token)
                        .build());
    }
}
