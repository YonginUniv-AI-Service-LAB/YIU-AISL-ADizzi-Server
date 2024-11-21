package AISL.ADizzi.service;


import AISL.ADizzi.dto.request.CreateSlotRequest;
import AISL.ADizzi.dto.request.UpdateSlotRequest;
import AISL.ADizzi.dto.response.SlotResponse;
import AISL.ADizzi.entity.Container;
import AISL.ADizzi.entity.Image;
import AISL.ADizzi.entity.Member;
import AISL.ADizzi.entity.Slot;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.exception.ErrorType;
import AISL.ADizzi.repository.ContainerRepository;
import AISL.ADizzi.repository.ImageRepository;
import AISL.ADizzi.repository.MemberRepository;
import AISL.ADizzi.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotService {

    public final SlotRepository slotRepository;
    public final MemberRepository memberRepository;
    public final ContainerRepository containerRepository;
    public final ImageRepository imageRepository;

    @Transactional
    public void createSlot(Long memberId, Long containerId, CreateSlotRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Container container = containerRepository.findById(containerId).orElseThrow(() -> new ApiException(ErrorType.CONTAINER_NOT_FOUND));

        if (slotRepository.existsByContainerAndTitle(container, request.getTitle())) {
            throw new ApiException(ErrorType.CONTAINER_ALREADY_EXISTS);
        }

        Image image = imageRepository.findById(request.getImageId()).orElseThrow(() -> new ApiException(ErrorType.IMAGE_NOT_FOUND));

        Slot slot = new Slot(
                container,
                request.getTitle(),
                image
        );

        // TODO: Default 슬롯 생성 코드 추가 필요 (슬롯 api작성 후)

        slotRepository.save(slot);
    }

    @Transactional
    public void updateSlot(Long memberId, Long SlotId, UpdateSlotRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Slot slot = slotRepository.findById(SlotId).orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));

        if (!slot.getContainer().getRoom().getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        if (request.getTitle() != null) {
            if (slotRepository.existsByContainerAndTitle(slot.getContainer(), request.getTitle())) {
                throw new ApiException(ErrorType.SLOT_ALREADY_EXISTS);
            }
            slot.setTitle(request.getTitle());
        }

        if (request.getImageId() != null) {
            Image image = imageRepository.findById(request.getImageId()).orElseThrow(() -> new ApiException(ErrorType.IMAGE_NOT_FOUND));
            slot.setImage(image);
        }

        slotRepository.save(slot);
    }

    @Transactional
    public void deleteSlot(Long memberId, Long slotId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ApiException(ErrorType.SLOT_NOT_FOUND));

        if (!slot.getContainer().getRoom().getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        slotRepository.delete(slot);
    }

    @Transactional(readOnly = true)
    public List<SlotResponse> getMySlot(Long containerId, String sortBy) {
        Container container = containerRepository.findById(containerId).orElseThrow(() -> new ApiException(ErrorType.CONTAINER_NOT_FOUND));
        List<Slot> slots;

        switch (sortBy.toLowerCase()) {
            case "old":
                slots = slotRepository.findByContainerOrderByUpdatedAtAsc(container);
                break;
            case "recent":
            default:
                slots = slotRepository.findByContainerOrderByUpdatedAtDesc(container);
                break;
        }
        return slots.stream().map(SlotResponse::new).collect(Collectors.toList());
    }

}
