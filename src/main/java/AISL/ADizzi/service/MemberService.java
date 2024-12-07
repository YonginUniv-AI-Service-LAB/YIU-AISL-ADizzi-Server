package AISL.ADizzi.service;

import AISL.ADizzi.dto.request.LoginRequest;
import AISL.ADizzi.dto.request.SignUpRequest;
import AISL.ADizzi.dto.request.UpdateMemberRequest;
import AISL.ADizzi.dto.response.LoginResponse;
import AISL.ADizzi.entity.Member;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.exception.ErrorType;
import AISL.ADizzi.repository.MemberRepository;
import AISL.ADizzi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorType.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = new Member(
                request.getEmail(),
                encodedPassword // 비밀번호 암호화 없이 그대로 저장
        );

        memberRepository.save(member);
    }

    public LoginResponse login(LoginRequest request) {
        // 이메일로 회원 정보 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));

        // 비밀번호가 일치하지 않으면 예외 발생
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new ApiException(ErrorType.INVALID_PASSWORD);
        }

        // Access Token과 Refresh Token 생성
        String accessToken = JwtUtil.generateAccessToken(member.getId());
        String refreshToken = JwtUtil.generateRefreshToken(member.getId());

        // LoginResponse 객체를 생성하여 반환
        return new LoginResponse(accessToken, refreshToken, member.getEmail());
    }

    @Transactional
    public void updateMember(UpdateMemberRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화 없이 그대로 업데이트
        }
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }

    public String refreshAccessToken(Long memberId) {
        return JwtUtil.generateAccessToken(memberId);
    }
}