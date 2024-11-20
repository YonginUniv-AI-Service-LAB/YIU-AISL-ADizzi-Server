package AISL.ADizzi.service;

import AISL.ADizzi.dto.request.CreateRoomRequest;
import AISL.ADizzi.dto.request.UpdateRoomRequest;
import AISL.ADizzi.dto.response.RoomResponse;
import AISL.ADizzi.entity.Member;
import AISL.ADizzi.entity.Room;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.exception.ErrorType;
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
public class RoomService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public void createRoom(Long memberId, CreateRoomRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));

        if (roomRepository.existsByMemberAndTitle(member, request.getTitle())) {
            throw new ApiException(ErrorType.ROOM_ALREADY_EXISTS);
        }

        Room room = new Room(
                member,
                request.getTitle(),
                request.getIcon(),
                request.getColor()
        );

        roomRepository.save(room);
    }

    @Transactional
    public void updateRoom(Long memberId, Long roomId, UpdateRoomRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ApiException(ErrorType.ROOM_NOT_FOUND));

        if (!room.getMember().equals(member)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        if (request.getTitle() != null) {
            if (roomRepository.existsByMemberAndTitle(member, request.getTitle())) {
                throw new ApiException(ErrorType.ROOM_ALREADY_EXISTS);
            }
            room.setTitle(request.getTitle());
        }

        if (request.getIcon() != null) {
            room.setIcon(request.getIcon());
        }

        if (request.getColor() != null) {
            room.setColor(request.getColor());
        }

        roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long memberId, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ApiException(ErrorType.ROOM_NOT_FOUND));

        if (!room.getMember().getId().equals(memberId)) {
            throw new ApiException(ErrorType.INVALID_AUTHOR);
        }

        roomRepository.delete(room);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getMyRooms(Long memberId, String sortBy) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorType.MEMBER_NOT_FOUND));
        List<Room> rooms;

        switch (sortBy.toLowerCase()) {
            case "old":
                rooms = roomRepository.findByMemberOrderByUpdatedAtAsc(member);
                break;
            case "recent":
            default:
                rooms = roomRepository.findByMemberOrderByUpdatedAtDesc(member);
                break;
        }

        return rooms.stream().map(RoomResponse::new).collect(Collectors.toList());
    }

}