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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    @Transactional
    public String UserSignup(String email, String password, String name){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return userRepository.findByEmail(email).get().getObjectId()+" already exist"; //상태코드 반환하도록 수정해야함.

        }
        User User = new User(email, password,name);
        userRepository.save(User);
        System.out.println(userRepository.findByEmail(email));
        return "완료";

    }

    @Transactional
    public JwtToken UserSignIn(String id, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        Authentication authentication;
        try{
            //loadUserByUsername 호출
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        }catch (BadCredentialsException e){
            e.printStackTrace();
            return null;
        }
        if(!authentication.isAuthenticated()){
            return null;
        }
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

}