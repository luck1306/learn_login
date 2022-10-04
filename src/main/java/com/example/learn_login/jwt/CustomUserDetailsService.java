package com.example.learn_login.jwt;

import com.example.learn_login.entity.CustomUserDetails;
import com.example.learn_login.entity.User;
import com.example.learn_login.entity.UserRepository;
import com.example.learn_login.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByAccountId(username).map(this::createUserDetails).orElseThrow(NotFoundException::new);
    }

    private UserDetails createUserDetails(User user) {
        return new CustomUserDetails(user);
    }
}
