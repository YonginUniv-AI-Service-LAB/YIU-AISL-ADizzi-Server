package AISL.ADizzi.controller;

import AISL.ADizzi.dto.request.CreateItemRequest;
import AISL.ADizzi.dto.request.UpdateItemRequest;
import AISL.ADizzi.dto.response.ItemResponse;
import AISL.ADizzi.service.ItemService;
import AISL.ADizzi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "물건 관련 API")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "메인(전체 아이템 조회). sortBy 옵션: 최신순(recent), 오래된순(old), 기본 값은 recent")
    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> getAllItems(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy)
    {
        Long memberId =JwtUtil.extractAccessToken(token);
        List<ItemResponse> items = itemService.getAllItems(memberId, sortBy);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "수납칸에 속한 물건 목록 조회. sortBy 옵션: 최신순(recent), 오래된순(old), 기본 값은 recent")
    @GetMapping("/room/{roomId}/container/{containerId}/slot/{slotId}/item")
    public ResponseEntity<List<ItemResponse>> getMyItems(
            @RequestHeader("Authorization") String token,
            @PathVariable Long slotId,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy)
    {
        List<ItemResponse> items = itemService.getMyItems(slotId, sortBy);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "수납칸에 해당하는 물건 카테고리별 조회")
    @GetMapping("/room/{roomId}/container/{containerId}/slot/{slotId}/item/category/{categoryId}")
    public ResponseEntity<List<ItemResponse>> getMyItemsByCategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Long slotId,
            @PathVariable Long categoryId)
    {
        Long memberId =JwtUtil.extractAccessToken(token);
        List<ItemResponse> items = itemService.getItemsByCategory(memberId, slotId, categoryId);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "수납칸에 물건 생성")
    @PostMapping("/room/{roomId}/container/{containerId}/slot/{slotId}/item")
    public ResponseEntity<String> createItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long slotId,
            @Valid @RequestBody CreateItemRequest request)
    {
        Long memberId = JwtUtil.extractAccessToken(token);
        itemService.createItem(memberId, slotId, request);
        return ResponseEntity.ok("success");
    }


    @Operation(summary ="물건 수정")
    @PutMapping("/room/{roomId}/container/{containerId}/slot/{slotId}/item/{itemId}")
    public ResponseEntity<String> updateItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateItemRequest request)
    {
        Long memberId = JwtUtil.extractAccessToken(token);
        itemService.updateItem(memberId, itemId, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "물건 이동")
    @PatchMapping("/room/{roomId}/container/{containerId}/slot/{slotId}/item/{itemId}")
    public ResponseEntity<String> moveItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long slotId,
            @PathVariable Long itemId
    ){
        Long memberId = JwtUtil.extractAccessToken(token);
        itemService.moveItem(memberId, slotId, itemId);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "물건 삭제")
    @DeleteMapping("/room/{roomId}/container/{containerId}/slot/{slotId}/item/{itemId}")
    public ResponseEntity<String> deleteItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long itemId)
    {
        Long memberId = JwtUtil.extractAccessToken(token);
        itemService.deleteItem(memberId, itemId);
        return ResponseEntity.ok("success");
    }


}
