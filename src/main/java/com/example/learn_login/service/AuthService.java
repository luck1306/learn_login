package com.example.learn_login.service;

import com.example.learn_login.dto.RequestForSignUp;
import com.example.learn_login.dto.TokenDto;
import com.example.learn_login.entity.User;
import com.example.learn_login.entity.UserRepository;
import com.example.learn_login.jwt.JwtProvider;
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

}
