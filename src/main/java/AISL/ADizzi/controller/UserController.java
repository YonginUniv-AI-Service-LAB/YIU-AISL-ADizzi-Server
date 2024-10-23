package AISL.ADizzi.controller;

import AISL.ADizzi.dto.JoinRequestDto;
import AISL.ADizzi.dto.JoinResponseDto;
import AISL.ADizzi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<JoinResponseDto> join(@Valid @RequestBody JoinRequestDto request) {
        JoinResponseDto response = userService.join(request);
        return ResponseEntity.ok(response);
    }
}