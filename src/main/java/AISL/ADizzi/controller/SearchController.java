package AISL.ADizzi.controller;

import AISL.ADizzi.dto.response.ItemResponse;
import AISL.ADizzi.dto.response.RoomResponse;
import AISL.ADizzi.dto.response.SearchResponse;
import AISL.ADizzi.service.SearchService;
import AISL.ADizzi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "검색 관련 API")
public class SearchController {

    private final SearchService searchService;


    @Operation(summary = "물건 검색 (물건의 이름과 설명으로 검색 가능)")
    @GetMapping("/search")
    public ResponseEntity <List<ItemResponse>> searchItems (
            @RequestHeader("Authorization") String token,
            @RequestParam("query") String query) {
        Long memberId = JwtUtil.extractAccessToken(token);
        List<ItemResponse> items = searchService.searchItems(memberId, query);
        return ResponseEntity.ok(items);
    }
}
