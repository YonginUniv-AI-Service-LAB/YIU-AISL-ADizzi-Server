package AISL.ADizzi.controller;

import AISL.ADizzi.dto.request.CreateContainerRequest;
import AISL.ADizzi.dto.request.UpdateContainerRequest;
import AISL.ADizzi.dto.response.ContainerResponse;
import AISL.ADizzi.service.ContainerService;
import AISL.ADizzi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "수납장 관련 API")
public class ContainerController {

    private final ContainerService containerService;

    @Operation(summary = "수납장 생성")
    @PostMapping("/room/{roomId}/container")
    public ResponseEntity<String> createContainer(@RequestHeader("Authorization") String token, @PathVariable Long roomId,  @Valid @RequestBody CreateContainerRequest request) {
        Long memberId = JwtUtil.extractAccessToken(token);
        containerService.createContainer(memberId, roomId, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "수납장 수정. 수정할 속성만 입력")
    @PutMapping("/room/{roomId}/container/{containerId}") // 게시글 수정
    public ResponseEntity<String> updateContainer(@RequestHeader("Authorization") String token, @PathVariable Long containerId, @Valid @RequestBody UpdateContainerRequest request) {
        Long memberId = JwtUtil.extractAccessToken(token);
        containerService.updateContainer(memberId, containerId, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "수납장 삭제")
    @DeleteMapping("/room/{roomId}/container/{containerId}") // 게시글 삭제
    public ResponseEntity<String> deleteContainer(@RequestHeader("Authorization") String token, @PathVariable Long containerId) {
        Long memberId = JwtUtil.extractAccessToken(token);
        containerService.deleteContainer(memberId, containerId);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "수납장 조회. sortBy 옵션: 최신순(recent), 오래된순(old), 기본 값은 recent")
    @GetMapping("/room/{roomId}/container") // 전체 게시글 조회
    public ResponseEntity<List<ContainerResponse>> getMyContainer(
            @RequestHeader("Authorization") String token,
            @PathVariable Long roomId,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy) {
        List<ContainerResponse> rooms = containerService.getMyContainer(roomId, sortBy);
        return ResponseEntity.ok(rooms);
    }
}