package AISL.ADizzi.repository;

import AISL.ADizzi.entity.Container;
import AISL.ADizzi.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContainerRepository extends JpaRepository<Container, Long> {

    // 방에 해당하는 수납장 최신순 조회
    List<Container> findByRoomOrderByUpdatedAtDesc(Room room);

    // 방에 해당하는 수납장 오래된 순 조회
    List<Container> findByRoomOrderByUpdatedAtAsc(Room room);

    // 이름으로 검색
    List<Container> findByTitleContaining(String query);
}
