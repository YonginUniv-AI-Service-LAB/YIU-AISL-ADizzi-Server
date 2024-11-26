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
import org.apache.commons.text.similarity.FuzzyScore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public List<ItemResponse> searchItems(Long userId, String query) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));

        List<Item> items = itemRepository.findByMember(member); // 사용자에 해당하는 모든 아이템 가져오기
        FuzzyScore fuzzyScore = new FuzzyScore(Locale.KOREA);

        return items.stream()
                .filter(item -> fuzzyScore.fuzzyScore(item.getTitle(), query) > 2 || // 유사도 점수 기준
                        fuzzyScore.fuzzyScore(item.getDetail(), query) > 2)
                .map(ItemResponse::new)
                .toList();
    }

}