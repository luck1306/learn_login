package com.example.learn_login.servlet;

import com.example.learn_login.entity.Role;
import com.example.learn_login.error.NotFoundException;
import com.example.learn_login.jwt.JwtProvider;
import com.example.learn_login.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminServlet", urlPatterns = "/auth/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminServlet extends HttpServlet {

    private final JwtProvider jwtProvider;

    private final UserRepository repository;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = jwtProvider.parseRequest(request);
        Claims claim = jwtProvider.tokenParser(token);
        Role role = repository.findByAccountId(claim.getSubject())
                .orElseThrow(() -> NotFoundException.EXCEPTION).getRole();
        if(!role.equals(Role.ADMIN)) {
            throw new RuntimeException("can approach admin only");
        }
    }
}
