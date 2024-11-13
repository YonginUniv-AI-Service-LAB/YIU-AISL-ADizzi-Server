package AISL.ADizzi.entity;

import com.zaxxer.hikari.util.UtilityElf;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이미지 ID

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 일시 yyyy-MM-dd HH:mm:ss

    @Column(nullable = false)
    private String imageUrl; // 이미지 URL

    @Column(nullable = false)
    private Long type; // 물건 카테고리 [room: 1, container: 2, slot: 3, item: 4]

    public Image(String imageUrl, Long type) {
        this.imageUrl = imageUrl;
        this.type = type;
        this.createdAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
    }
}

