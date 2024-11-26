package AISL.ADizzi.service;

import AISL.ADizzi.dto.request.CreateContainerRequest;
import AISL.ADizzi.dto.request.UpdateContainerRequest;
import AISL.ADizzi.dto.response.ContainerResponse;
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
public class ContainerService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final ContainerRepository containerRepository;
    private final ImageRepository imageRepository;
    private final SlotRepository slotRepository;
    private final ImageService imageService;

    @Transactional
    public void createContainer(Long memberId, Long roomId, CreateContainerRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ApiException(ErrorType.ROOM_NOT_FOUND));

        if (containerRepository.existsByRoomAndTitle(room, request.getTitle())) {
            throw new ApiException(ErrorType.CONTAINER_ALREADY_EXISTS);
        }

        Image image = imageRepository.findById(request.getImageId()).orElseThrow(() -> new ApiException(ErrorType.IMAGE_NOT_FOUND));

        if (containerRepository.existsByImage(image)) {
            throw new ApiException(ErrorType.IMAGE_ALREADY_USED);
        }

        Container container = new Container(
                room,
                request.getTitle(),
                image
        );

        Slot slot = new Slot(
                container,
                " ",
                image
        );

        containerRepository.save(container);
        slotRepository.save(slot);

    }

    @Transactional
    public void updateContainer(Long memberId, Long containerId, UpdateContainerRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Container container = containerRepository.findById(containerId).orElseThrow(() -> new ApiException(ErrorType.CONTAINER_NOT_FOUND));

        if (!container.getRoom().getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        if (request.getTitle() != null) {
            if (containerRepository.existsByRoomAndTitle(container.getRoom(), request.getTitle())) {
                throw new ApiException(ErrorType.CONTAINER_ALREADY_EXISTS);
            }
            container.setTitle(request.getTitle());
        }

        if (request.getImageId() != null) {
            Image image = imageRepository.findById(request.getImageId()).orElseThrow(() -> new ApiException(ErrorType.IMAGE_NOT_FOUND));
            if (!containerRepository.findByImage(image).equals(container)){
                throw new ApiException(ErrorType.IMAGE_ALREADY_USED);
            }
            container.setImage(image);
        }

        container.setUpdatedAt(LocalDateTime.now());
        containerRepository.save(container);
    }

    @Transactional
    public void deleteContainer(Long memberId, Long containerId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Container container = containerRepository.findById(containerId).orElseThrow(() -> new ApiException(ErrorType.CONTAINER_NOT_FOUND));

        if (!container.getRoom().getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        Image image = container.getImage();
        imageService.deleteImageFromS3(image.getImageUrl());
        imageRepository.delete(image);

        containerRepository.delete(container);
    }

    @Transactional(readOnly = true)
    public List<ContainerResponse> getMyContainer(Long roomId, String sortBy) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ApiException(ErrorType.ROOM_NOT_FOUND));
        List<Container> containers;

        switch (sortBy.toLowerCase()) {
            case "old":
                containers = containerRepository.findByRoomOrderByUpdatedAtAsc(room);
                break;
            case "recent":
            default:
                containers = containerRepository.findByRoomOrderByUpdatedAtDesc(room);
                break;
        }

        List<ContainerResponse> containerResponses = containers.stream()
                .map(container -> {
                    // 조건에 맞는 슬롯을 찾기
                    Slot slot = slotRepository.findDistinctFirstByContainerAndTitle(container, " ");
                    Long slotId = (slot != null) ? slot.getId() : null; // 슬롯이 없으면 null
                    return new ContainerResponse(container, slotId); // 슬롯 ID 포함하여 생성
                })
                .collect(Collectors.toList());

        return containerResponses;
    }
}
