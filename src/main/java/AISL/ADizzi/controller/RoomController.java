package AISL.ADizzi.controller;

import AISL.ADizzi.dto.request.CreateRoomRequest;
import AISL.ADizzi.dto.request.UpdateRoomRequest;
import AISL.ADizzi.dto.response.RoomResponse;
import AISL.ADizzi.service.RoomService;
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
@Tag(name = "방 관련 API")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "방 생성")
    @PostMapping("/room")
    public ResponseEntity<String> createRoom(@RequestHeader("Authorization") String token, @Valid @RequestBody CreateRoomRequest request) {
        Long memberId = JwtUtil.extractAccessToken(token);
        roomService.createRoom(memberId, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "방 수정. 수정할 속성만 입력")
    @PutMapping("/room/{room_id}") // 게시글 수정
    public ResponseEntity<String> updateRoom(@RequestHeader("Authorization") String token, @PathVariable Long room_id, @Valid @RequestBody UpdateRoomRequest request) {
        Long memberId = JwtUtil.extractAccessToken(token);
        roomService.updateRoom(memberId, room_id, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "방 삭제")
    @DeleteMapping("/room/{room_id}") // 게시글 삭제
    public ResponseEntity<String> deleteRoom(@RequestHeader("Authorization") String token, @PathVariable Long room_id) {
        Long memberId = JwtUtil.extractAccessToken(token);
        roomService.deleteRoom(memberId, room_id);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "방 조회. sortBy 옵션: 최신순(recent), 오래된순(old), 기본 값은 recent")
    @GetMapping("/room") // 전체 게시글 조회
    public ResponseEntity<List<RoomResponse>> getMyRooms(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy) {
        Long memberId = JwtUtil.extractAccessToken(token);
        List<RoomResponse> rooms = roomService.getMyRooms(memberId, sortBy);
        return ResponseEntity.ok(rooms);
    }
}
