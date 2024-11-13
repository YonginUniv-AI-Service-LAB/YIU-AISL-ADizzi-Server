package AISL.ADizzi.repository;

import AISL.ADizzi.entity.Container;
import AISL.ADizzi.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    // 수납장에 해당하는 수납칸 최신순 조회
    List<Slot> findByContainerOrderByUpdatedAtDesc(Container container);

    // 수납장에 해당하는 수납칸 오래된 순 조회
    List<Slot> findByContainerOrderByUpdatedAtAsc(Container container);

    // 이름으로 검색
    List<Slot> findByTitleContaining(String query);
}
