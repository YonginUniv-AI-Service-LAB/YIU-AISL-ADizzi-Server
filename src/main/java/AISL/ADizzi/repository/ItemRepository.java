package AISL.ADizzi.repository;

import AISL.ADizzi.entity.Container;
import AISL.ADizzi.entity.Item;
import AISL.ADizzi.entity.Member;
import AISL.ADizzi.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // 수납칸에 해당하는 물건 최신순 조회
    List<Item> findBySlotOrderByUpdatedAtDesc(Slot slot);

    // 수납칸에 해당하는 물건 오래된 순 조회
    List<Item> findBySlotOrderByUpdatedAtAsc(Slot slot);

    // 이름으로 검색
    List<Item> findByTitleContaining(String query);

    // 설명으로 검색
    List<Item> findByDetailContaining(String query);

    // 수납칸에 해당하는 물건 카테고리로 조회
    @Query("SELECT i FROM Item i WHERE i.slot = :slot AND i.category = :category")
    List<Item> findBySlotAndCategory(Slot slot, @Param("category") Long category);

    // 해당 수납칸에 같은 이름의 물건이 있는지 검색
    boolean existsBySlotAndTitle(Slot slot, String title);

    int countBySlotAndTitle(Slot targetSlot, String title);
}
