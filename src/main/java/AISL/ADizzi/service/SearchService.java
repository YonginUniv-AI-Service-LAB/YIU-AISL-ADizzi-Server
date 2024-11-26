package AISL.ADizzi.service;

import AISL.ADizzi.dto.response.ItemResponse;
import AISL.ADizzi.dto.response.SearchResponse;
import AISL.ADizzi.entity.Item;
import AISL.ADizzi.entity.Member;
import AISL.ADizzi.entity.Room;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.exception.ErrorType;
import AISL.ADizzi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public List<ItemResponse> searchItems(Long memberId, String query) {
        {
            // 검색어가 null이거나 빈 문자열일 경우 예외 처리
            if (query == null || query.isBlank()) {
                throw new ApiException(ErrorType.MISSING_SEARCH_KEYWORD);
            }

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));

            // 검색 쿼리로 물건 조회
            List<Item> items = itemRepository.findByUserAndQuery(memberId, query);

            // 검색 결과가 없을 경우 예외 발생
            if (items.isEmpty()) {
                throw new ApiException(ErrorType.NO_SEARCH_RESULTS);
            }
            // 결과를 ItemResponse로 변환하여 반환
            return items.stream()
                    .map(ItemResponse::new)
                    .toList();
        }
    }

}