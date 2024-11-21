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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "아이템 관련 API")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "수납칸에 속한 물건 목록 조회(최신순,오래된순)")
    @GetMapping("/room/{room_id}/container/{container_id}/slot/{slot_id}")
    public ResponseEntity<List<ItemResponse>> getMyItems(
            @RequestHeader("Authorization") String token,
            @PathVariable Long slot_id,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy)
    {
        Long memberId = JwtUtil.extractAccessToken(token);
        List<ItemResponse> items = itemService.getMyItems(memberId,slot_id,sortBy);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "수납칸에 물건 생성")
    @PostMapping("/room/{room_id}/container/{container_id}/slot/{slot_id}/item")
    public ResponseEntity<String> createItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long slot_id,
            @Valid @RequestBody CreateItemRequest request)
    {

        Long memberId = JwtUtil.extractAccessToken(token);
        itemService.createItem(memberId,slot_id,request);
        return ResponseEntity.ok("success");
    }


    @Operation(summary ="물건 수정")
    @PutMapping("/room/{room_id}/container/{container_id}/slot/{slot_id}/item/{item_id}")
    public ResponseEntity<String> updateItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long slot_id,
            @PathVariable Long item_id,
            @Valid @RequestBody UpdateItemRequest request)
    {

        Long memberId = JwtUtil.extractAccessToken(token);
        itemService.updateItem(memberId, slot_id, item_id, request);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "물건 삭제")
    @DeleteMapping("/room/{room_id}/container/{container_id}/slot/{slot_id}/item/{item_id}")
    public ResponseEntity<String> deleteItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long item_id)
    {
        Long memberId = JwtUtil.extractAccessToken(token);
        itemService.deleteItem(memberId,item_id);
        return ResponseEntity.ok("success");
    }





}
