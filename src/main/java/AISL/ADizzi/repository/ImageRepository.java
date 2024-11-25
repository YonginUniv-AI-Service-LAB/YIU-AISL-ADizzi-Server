package AISL.ADizzi.repository;

import AISL.ADizzi.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i " +
            "LEFT JOIN Container c ON i.id = c.image.id " +
            "LEFT JOIN Slot s ON i.id = s.image.id " +
            "LEFT JOIN Item it ON i.id = it.image.id " +
            "WHERE c.image IS NULL AND s.image IS NULL AND it.image IS NULL")
    List<Image> findUnusedImages();
}
