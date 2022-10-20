package com.example.learn_login.service;

import com.example.learn_login.dto.request.RequestForSignUp;
import com.example.learn_login.dto.response.TokenDto;
import com.example.learn_login.entity.RefreshToken;
import com.example.learn_login.entity.User;
import com.example.learn_login.repository.RefreshTokenRepository;
import com.example.learn_login.repository.UserRepository;
import com.example.learn_login.error.ForbiddenException;
import com.example.learn_login.error.NotFoundException;
import com.example.learn_login.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshRepository;

    public void signUp(RequestForSignUp request) {
        userRepository.save(User.builder()
            .accountId(request.getAccountId())
            .password(passwordEncoder.encode(request.getPassword()))
            .build());
    }

    public TokenDto signIn(RequestForSignUp request) {
        User user = userRepository.findByAccountId(request.getAccountId()).orElseThrow(NotFoundException::new);

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new NotFoundException();
        }
        return TokenDto.builder()
                .accessToken(jwtProvider.generateAccessToken(user.getAccountId()))
                .refreshToken(jwtProvider.generateRefreshToken(user.getAccountId()))
                .build();
    }

    @Transactional
    public TokenDto issuance(String refresh) {
        if (!jwtProvider.isNonExpired(refresh)) {
            throw ForbiddenException.EXCEPTION;
        }
        String accountId = jwtProvider.tokenParser(refresh).getSubject();
        if(!refreshRepository.existsById(accountId)) {
            throw new NotFoundException();
        }
        return TokenDto.builder()
                .accessToken(jwtProvider.generateAccessToken(accountId))
                .refreshToken(jwtProvider.generateRefreshToken(accountId))
                .build();
    }

    public void logout(String refresh) {
        Claims claim = jwtProvider.tokenParser(refresh);
        RefreshToken token = refreshRepository.findById(claim.getSubject()).orElseThrow(NotFoundException::new);
        refreshRepository.delete(token);
        // when don't receive refresh token
//        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
//        RefreshToken refreshToken = refreshRepo.findByKeyA(userId).orElseThrow(NotFoundException::new);
//        refreshRepo.delete(refreshToken);
    }
}
