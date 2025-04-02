package com.checkmate.ai.controller;

import com.checkmate.ai.dto.JwtToken;
import com.checkmate.ai.dto.SignInDto;
import com.checkmate.ai.dto.SignUpDto;
import com.checkmate.ai.entity.User;
import com.checkmate.ai.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/sign-up")
    public String signup(@RequestBody SignUpDto signUpDto){
        String email = signUpDto.getEmail();
        String password = signUpDto.getPassword();
        String name = signUpDto.getName();
        String result = userService.signUp(email,password,name);
        log.info("회원가입 결과 :"+result);
        return result;
    }

    @PostMapping("/sign-in")
    public JwtToken signin(@RequestBody SignInDto signInDto){
        String id = signInDto.getEmail();
        String password = signInDto.getPassword();

        JwtToken jwtToken = userService.signIn(id, password);
        if(jwtToken == null){
            log.info("인증 실패");
            return null;
        }else{
            log.info("로그인 성공");
            return jwtToken;
        }
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> readAllUsers() {
        userService.deleteAll();
        return ResponseEntity.ok("✅ 모든 로그가 삭제되었습니다.");
    }

}