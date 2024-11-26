package AISL.ADizzi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
    }
}
