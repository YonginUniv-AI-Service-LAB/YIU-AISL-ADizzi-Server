package AISL.ADizzi.service;

import AISL.ADizzi.dto.request.CreateContainerRequest;
import AISL.ADizzi.dto.request.UpdateContainerRequest;
import AISL.ADizzi.dto.response.ContainerResponse;
import AISL.ADizzi.entity.Container;
import AISL.ADizzi.entity.Image;
import AISL.ADizzi.entity.Member;
import AISL.ADizzi.entity.Room;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.exception.ErrorType;
import AISL.ADizzi.repository.ContainerRepository;
import AISL.ADizzi.repository.ImageRepository;
import AISL.ADizzi.repository.MemberRepository;
import AISL.ADizzi.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void createContainer(Long memberId, Long roomId, CreateContainerRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ApiException(ErrorType.ROOM_NOT_FOUND));

        if (containerRepository.existsByRoomAndTitle(room, request.getTitle())) {
            throw new ApiException(ErrorType.CONTAINER_ALREADY_EXISTS);
        }

        Image image = imageRepository.findById(request.getImageId()).orElseThrow(() -> new ApiException(ErrorType.IMAGE_NOT_FOUND));

        Container container = new Container(
                room,
                request.getTitle(),
                image
        );

        // TODO: Default 슬롯 생성 코드 추가 필요 (슬롯 api작성 후)

        containerRepository.save(container);
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
            container.setImage(image);
        }

        containerRepository.save(container);
    }

    @Transactional
    public void deleteContainer(Long memberId, Long containerId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Container container = containerRepository.findById(containerId).orElseThrow(() -> new ApiException(ErrorType.CONTAINER_NOT_FOUND));

        if (!container.getRoom().getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

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
        return containers.stream().map(ContainerResponse::new).collect(Collectors.toList());
    }

}
