package AISL.ADizzi.service;

import AISL.ADizzi.dto.response.TreeResponse;
import AISL.ADizzi.entity.*;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.exception.ErrorType;
import AISL.ADizzi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreeService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    public List<TreeResponse> getMyTree(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));

        // 해당 멤버의 방 목록 가져오기
        List<Room> rooms = roomRepository.findByMemberOrderByTitle(member); // 이 메서드는 Member에 따라 방을 찾는 쿼리 메서드입니다.

        // 방 목록을 TreeResponse로 변환
        return rooms.stream()
                .map(TreeResponse::new) // Room 객체를 TreeResponse로 변환
                .collect(Collectors.toList());

    }

}
