package AISL.ADizzi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import AISL.ADizzi.dto.request.LoginRequest;
import AISL.ADizzi.dto.request.SignUpRequest;
import AISL.ADizzi.dto.request.UpdateMemberRequest;
import AISL.ADizzi.dto.response.LoginResponse;
import AISL.ADizzi.service.MemberService;
import AISL.ADizzi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "[API 연동 테스트 용]현재 시간 조회")
    @GetMapping("/time")
    public ResponseEntity<String> getCurrentTime() {
        // 현재 시간을 가져와서 특정 형식으로 포맷
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        return ResponseEntity.ok(formattedNow);
    }

    @Operation(summary = "[로그인 전]로그인")
    @PostMapping("/login") // 로그인
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @Operation(summary = "[로그인 전]회원 가입")
    @PostMapping("/signUp") // 회원 가입
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest request) {
        memberService.signUp(request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "비밀번호 재설정")
    @PutMapping("/user") // 회원 정보 수정
    public ResponseEntity<String> updateMember(@RequestBody UpdateMemberRequest request) {
        memberService.updateMember(request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "[로그인 후]회원 탈퇴")
    @DeleteMapping("/user") // 회원 탈퇴
    public ResponseEntity<String> deleteMember(@RequestHeader("Authorization") String token) {
        Long memberId = JwtUtil.extractAccessToken(token);
        memberService.deleteMember(memberId);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "[엑세스 토큰 만료 시]리프레시 토큰으로 엑세스 토큰 재발급(엑세스 토큰 만료 기간: 30분)")
    @GetMapping("/refreshAccessToken") // 리프레시 토큰 받아서 엑세스 토큰 반환
    public ResponseEntity<String> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        Long memberId = JwtUtil.extractAccessToken(refreshToken);
        return ResponseEntity.ok(memberService.refreshAccessToken(memberId));
    }

}
