package com.example.learn_login.controller;

import com.example.learn_login.dto.request.RequestForSignUp;
import com.example.learn_login.dto.response.TokenDto;
import com.example.learn_login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid RequestForSignUp request){
        authService.signUp(request);
    }

    @PostMapping("/sign-in")
    public TokenDto singIn(@RequestBody RequestForSignUp request) {
        return authService.signIn(request);
    }

    @PatchMapping("/refresh")
    public TokenDto refresh(@RequestHeader("Refresh") String refresh) {
        return authService.issuance(refresh);
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Refresh") @Valid String refresh) {
        authService.logout(refresh);
    }
}