package com.checkmate.ai.service;


import com.checkmate.ai.dto.CustomUserDetails;
import com.checkmate.ai.entity.User;
import com.checkmate.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //이 함수로 username에 해당하는 사용자를 찾아서 인증객체를 만듬.
    //비밀번호 비교는 시큐리티가 내부적으로 getPassword를 통해서 비교함. 그래서 customuserdetails에 아이디, 비밀번호는 무조건 있어야 하고 getter를 설정해야 하는 이유임.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(User user) {
        return CustomUserDetails.builder()
                .username(user.getEmail())
                .user_id(user.getObjectId())
                .name(user.getName())
                .password(user.getPassword()) // ✅ 인코딩하지 않고 그대로 사용
                .build();
    }

}