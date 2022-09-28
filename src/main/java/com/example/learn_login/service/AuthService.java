package com.example.learn_login.service;

import com.example.learn_login.dto.request.RequestForSignUp;
import com.example.learn_login.dto.response.TokenDto;
import com.example.learn_login.entity.RefreshRepo;
import com.example.learn_login.entity.RefreshToken;
import com.example.learn_login.entity.User;
import com.example.learn_login.entity.UserRepository;
import com.example.learn_login.exception.ForbiddenException;
import com.example.learn_login.exception.NotFoundException;
import com.example.learn_login.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshRepo refreshRepo;
    public void signUp(RequestForSignUp request) {
        userRepository.save(User.builder()
            .accountId(request.getAccountId())
            .password(passwordEncoder.encode(request.getPassword()))
            .build());
    }

    public TokenDto signIn(RequestForSignUp request) {
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthenticationToken();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return TokenDto.builder()
                .accessToken(jwtProvider.generateAccessToken(authentication))
                .refreshToken(jwtProvider.generateRefreshToken(authentication))
                .build();
    }

    @Transactional
    public TokenDto issuance(String refresh) {
        if (jwtProvider.isNonExpired(refresh)) {
            throw new ForbiddenException();
        }
        RefreshToken refreshToken = refreshRepo.findByKeyA(jwtProvider.tokenParser(refresh).getSubject())
                .orElseThrow(NotFoundException::new);

        UsernamePasswordAuthenticationToken authenticationToken = // principal : jwtProvider.getCurrentAccountId()
                new UsernamePasswordAuthenticationToken("account", "");

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(jwtProvider.generateAccessToken(authenticationToken))
                .refreshToken(jwtProvider.generateRefreshToken(authenticationToken))
                .build();

        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshRepo.save(refreshToken);
        return tokenDto;
    }

    @Transactional
    public void logout(String refresh) {
        Claims claim = jwtProvider.tokenParser(refresh);
        RefreshToken token = refreshRepo.findByKeyA(claim.getSubject()).orElseThrow(NotFoundException::new);
        refreshRepo.delete(token);
        // when don't receive refresh token
//        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
//        RefreshToken refreshToken = refreshRepo.findByKeyA(userId).orElseThrow(NotFoundException::new);
//        refreshRepo.delete(refreshToken);
    }
}
