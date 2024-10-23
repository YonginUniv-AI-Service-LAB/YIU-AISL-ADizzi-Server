package AISL.ADizzi.service;

import AISL.ADizzi.domain.User;
import AISL.ADizzi.dto.JoinRequestDto;
import AISL.ADizzi.dto.JoinResponseDto;
import AISL.ADizzi.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JoinResponseDto join(JoinRequestDto request) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화 및 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        return new JoinResponseDto(savedUser);
    }
}
