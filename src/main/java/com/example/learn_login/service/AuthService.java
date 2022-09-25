package com.example.learn_login.service;

import com.example.learn_login.dto.request.RequestForSignUp;
import com.example.learn_login.dto.response.TokenDto;
import com.example.learn_login.dto.response.UpdateRefreshResponse;
import com.example.learn_login.entity.RefreshRepo;
import com.example.learn_login.entity.RefreshToken;
import com.example.learn_login.entity.User;
import com.example.learn_login.entity.UserRepository;
import com.example.learn_login.exception.NotFoundException;
import com.example.learn_login.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public TokenDto signIn(RequestForSignUp request) { // if already be in refresh token db, update refresh token
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthenticationToken();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return TokenDto.builder()
                .accessToken(jwtProvider.generateAccessToken(authentication))
                .refreshToken(jwtProvider.generateRefreshToken(authentication))
                .build();
    }

    public UpdateRefreshResponse refresh(String refresh) {
        Claims claim = jwtProvider.tokenParser(refresh);
        RefreshToken refreshToken = refreshRepo.findByKeyA(claim.getSubject()).orElseThrow(NotFoundException::new);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(jwtProvider.getCurrentAccountId(), "");

        refreshToken.updateValue(jwtProvider.generateRefreshToken(authenticationToken));
        refreshRepo.save(refreshToken);

        return UpdateRefreshResponse.builder()
                .before(refresh)
                .after(refreshToken.getValueA())
                .build();
    }
}
