package AISL.ADizzi.service;

import AISL.ADizzi.dto.request.CreateItemRequest;
import AISL.ADizzi.dto.request.UpdateItemRequest;
import AISL.ADizzi.dto.response.ItemResponse;
import AISL.ADizzi.entity.Image;
import AISL.ADizzi.entity.Item;
import AISL.ADizzi.entity.Member;
import AISL.ADizzi.entity.Slot;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.exception.ErrorType;
import AISL.ADizzi.repository.ImageRepository;
import AISL.ADizzi.repository.ItemRepository;
import AISL.ADizzi.repository.MemberRepository;
import AISL.ADizzi.repository.SlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final SlotRepository slotRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;


    // 수납칸에 해당하는 물건 목록 최신순, 오래된순
    @Transactional
    public List<ItemResponse> getMyItems(Long slotId, String sortBy) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));
        List<Item> items;

        switch (sortBy.toLowerCase()) {
            case "old":
                items = itemRepository.findBySlotOrderByUpdatedAtAsc(slot);
                break;
            case "recent":
            default:
                items = itemRepository.findBySlotOrderByUpdatedAtDesc(slot);
                break;
        }

        return items.stream().map(ItemResponse::new).collect(Collectors.toList());
    }

    // 수납칸에 해당하는 물건 카테고리별 조회
    @Transactional
    public List<ItemResponse> getItemsByCategory(Long memberId, Long slotId, Long categoryId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));

        List<Item> items = itemRepository.findBySlotAndCategory(slot, categoryId);
        return items.stream().map(ItemResponse::new).collect(Collectors.toList());
    }


    // 수납칸에 해당하는 물건 생성
    @Transactional
    public void createItem(Long memberId, Long slotId, CreateItemRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));

        if (itemRepository.existsBySlotAndTitle(slot, request.getTitle())) {
            throw new ApiException(ErrorType.ITEM_ALREADY_EXISTS);
        }

        Image image = imageRepository.findById(request.getImageId()).orElseThrow(() -> new ApiException(ErrorType.IMAGE_NOT_FOUND));

        Item item = new Item(
                slot,
                request.getTitle(),
                request.getDetail(),
                image,
                request.getCategory(),
                member
        );

        itemRepository.save(item);
    }


    // 물건 수정
    @Transactional
    public void updateItem(Long memberId, Long itemId, UpdateItemRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ErrorType.ITEM_NOT_FOUND));

        if(!item.getMember().equals(member)){
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        if(request.getTitle() != null) {
            if (itemRepository.existsBySlotAndTitle(item.getSlot(), request.getTitle())) {
                throw new ApiException(ErrorType.ITEM_ALREADY_EXISTS);
            }
            item.setTitle(request.getTitle());
        }

        if (request.getDetail() != null) {
            item.setDetail(request.getDetail());
        }

        if (request.getCategory() != null) {
            item.setCategory(request.getCategory());
        }

        if (request.getImageId() != null) {
            Image image = imageRepository.findById(request.getImageId())
                    .orElseThrow(() -> new ApiException(ErrorType.IMAGE_NOT_FOUND));
            item.setImage(image);
        }

        item.setUpdatedAt(LocalDateTime.now());
        itemRepository.save(item);
    }

    // 물건 삭제
    @Transactional
    public void deleteItem(Long memberId, Long ItemId) {

        Member member = memberRepository.findById(memberId).orElseThrow(()->new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Item item = itemRepository.findById(ItemId).orElseThrow(() -> new ApiException(ErrorType.ITEM_NOT_FOUND));

        if (!item.getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        itemRepository.delete(item);
    }

    // 물건 이동
    @Transactional
    public void moveItem(Long memberId, Long slotId, Long itemId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ErrorType.ITEM_NOT_FOUND));

        // 권한 확인
        if (!item.getMember().equals(member) || !slot.getContainer().getRoom().getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        // 물건을 옮길 수납칸에 중복된 title이 있을 경우 "title(1)" 형태로 수정
        String baseTitle = item.getTitle();  // 원본 title
        String newTitle = baseTitle;  // 새로운 제목 (기본값은 원본 제목)
        int index = 1;

        while (itemRepository.existsBySlotAndTitle(slot, newTitle)) {
            newTitle = baseTitle + "(" + index + ")";
            index++;
        }

        item.setTitle(newTitle);
        item.setSlot(slot);
        item.setUpdatedAt(LocalDateTime.now());

        itemRepository.save(item);
    }

}

