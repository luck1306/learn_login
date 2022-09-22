package com.example.learn_login.service;

import com.example.learn_login.dto.RequestForSignUp;
import com.example.learn_login.entity.User;
import com.example.learn_login.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    public void signUp(RequestForSignUp request) {
        userRepository.save(User.builder()
            .accountId(request.getAccountId())
            .password(passwordEncoder.encode(request.getPassword()))
            .build());
    }

}
