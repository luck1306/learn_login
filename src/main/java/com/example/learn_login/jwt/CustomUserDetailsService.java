package com.example.learn_login.jwt;

import com.example.learn_login.entity.User;
import com.example.learn_login.entity.UserRepository;
import com.example.learn_login.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByAccountId(username).map(this::createUserDetails).orElseThrow(NotFoundException::new);
    }

    public UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().toString());
        return new org.springframework.security.core.userdetails.User(user.getAccountId(), user.getPassword(), Collections.singleton(grantedAuthority));
    }
}
