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
import AISL.ADizzi.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {


    private final SlotRepository slotRepository;
    private final ItemRepository itemRepository;
    private final ImageUploadService imageUploadService;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;



    // 슬롯에 해당하는 아이템 목록 최신순, 오래된순
    @Transactional
    public List<ItemResponse> getMyItems(String token,Long slotId, String sortBy) {
        // 토큰 유효성 검사 및 사용자 ID 추출
        JwtUtil.extractAccessToken(token);

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

    // 아이템 카테고리별 조회


    // 슬롯에 해당하는 아이템 생성
    @Transactional
    public void createItem(String token, Long slotId, CreateItemRequest request, MultipartFile imageFile) {
        // 토큰 유효성 검사
        JwtUtil.extractAccessToken(token);
        //  slotId에 해당하는 슬롯 조회
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));

        //  슬롯에 동일한 title을 가진 아이템이 이미 존재하는지 확인
        if (itemRepository.existsBySlotAndTitle(slot, request.getTitle())) {
            throw new ApiException(ErrorType.ITEM_ALREADY_EXISTS);
        }

        // 이미지 저장 및 Image 엔티티 생성
        Image image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            // MultipartFile로 이미지 업로드 및 URL 생성
            image = imageUploadService.saveImage(imageFile, 4L); // 4: item type
        }

        // 5. 아이템 객체 생성
        Item item = new Item(
                slot,
                request.getTitle(),
                request.getDetail(),
                image, // Image 엔티티 추가
                request.getCategory()
        );

        itemRepository.save(item);
    }


    // 컨테이너에 해당하는 아이템 생성


    // 아이템 수정
    @Transactional
    public void updateItem(String token,Long slotId,Long itemId, UpdateItemRequest request) {
        // 토큰 유효성 검사 및 사용자 ID 추출
        Long memberId = JwtUtil.extractAccessToken(token);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ErrorType.ITEM_NOT_FOUND));

        // 슬롯 조회
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));
        // 사용자가 슬롯에 접근할 권한이 있는지 검사
        if(!item.getSlot().getContainer().getRoom().getMember().getId().equals(memberId)){
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }
        //슬롯에 동이한 title을 가진 아이템이 있는지 중복 검사
            if (itemRepository.existsBySlotAndTitle(slot,request.getTitle())) {
                throw new ApiException(ErrorType.ITEM_ALREADY_EXISTS);
            }
            item.setTitle(request.getTitle());

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


    @Transactional
    public void deleteItem(String token, Long slotId) {
        Long memberId = JwtUtil.extractAccessToken(token);
        Item item = itemRepository.findById(slotId).orElseThrow(() -> new ApiException(ErrorType.ITEM_NOT_FOUND));
        if (!item.getSlot().getContainer().getRoom().getMember().getId().equals(memberId)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        itemRepository.delete(item);
    }




}

