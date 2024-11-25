package AISL.ADizzi.controller;

import AISL.ADizzi.dto.request.CreateSlotRequest;
import AISL.ADizzi.dto.request.UpdateSlotRequest;
import AISL.ADizzi.dto.response.SlotResponse;
import AISL.ADizzi.service.SlotService;
import AISL.ADizzi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "수납칸 관련 API")
public class SlotController {

    private final SlotService slotService;

    @Operation(summary = "수납칸 생성")
    @PostMapping("/room/{roomId}/container/{containerId}/slot")
    public ResponseEntity<String> createSlot(@RequestHeader("Authorization") String token, @PathVariable Long containerId,  @Valid @RequestBody CreateSlotRequest request) {
        Long memberId = JwtUtil.extractAccessToken(token);
        slotService.createSlot(memberId, containerId, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "수납칸 수정. 수정할 속성만 입력")
    @PutMapping("/room/{roomId}/container/{containerId}/slot/{slotId}") // 게시글 수정
    public ResponseEntity<String> updateSlot(@RequestHeader("Authorization") String token, @PathVariable Long slotId, @Valid @RequestBody UpdateSlotRequest request) {
        Long memberId = JwtUtil.extractAccessToken(token);
        slotService.updateSlot(memberId, slotId, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "수납칸 삭제")
    @DeleteMapping("/room/{roomId}/container/{containerId}/slot/{slotId}") // 게시글 삭제
    public ResponseEntity<String> deleteSlot(@RequestHeader("Authorization") String token, @PathVariable Long slotId) {
        Long memberId = JwtUtil.extractAccessToken(token);
        slotService.deleteSlot(memberId, slotId);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "수납칸 조회. sortBy 옵션: 최신순(recent), 오래된순(old), 기본 값은 recent")
    @GetMapping("/room/{roomId}/container/{containerId}/slot") // 전체 게시글 조회
    public ResponseEntity<Map<String, Object>> getMySlot(
            @RequestHeader("Authorization") String token,
            @PathVariable Long containerId,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy) {
        Map<String, Object> response = slotService.getMySlot(containerId, sortBy);
        return ResponseEntity.ok(response);
    }
}
