package AISL.ADizzi.repository;

import AISL.ADizzi.entity.Member;
import AISL.ADizzi.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface RoomRepository extends JpaRepository<Room, Long> {

    // 최신순으로 정렬
    List<Room> findByMemberOrderByUpdatedAtDesc(Member member);

    // 오래된순 정렬
    List<Room> findByMemberOrderByUpdatedAtAsc(Member member);

    // 이름순 정렬
    List<Room> findByMemberOrderByTitle(Member member);

    // 이름으로 검색
    List<Room> findByTitleContaining(String query);

    // 사용자에게 같은 이름의 방이 있는지 검색
    boolean existsByMemberAndTitle(Member member, String title);
}
