package com.checkmate.ai.service;

import com.checkmate.ai.dto.JwtToken;
import com.checkmate.ai.entity.User;
import com.checkmate.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public String signUp(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            return "User ID already exists!";
        }

        String encodedPassword = passwordEncoder.encode(password); // 🔥 비밀번호 암호화
        User currentUser = new User(email, encodedPassword, name);
        userRepository.save(currentUser);


        return "Sign-up successful";
    }

    @Transactional
    public JwtToken signIn(String email, String password) { // ✅ email로 변경
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password); // ✅ email 사용

        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return null;
        }

        if (!authentication.isAuthenticated()) {
            return null;
        }

        return jwtTokenProvider.generateToken(authentication);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}