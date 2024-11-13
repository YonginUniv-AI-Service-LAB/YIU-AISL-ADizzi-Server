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
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 물건 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot; // 물건이 위치한 수납칸

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 일시 yyyy-MM-dd HH:mm:ss

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 일시 yyyy-MM-dd HH:mm:ss

    @Column(nullable = false)
    private String title; // 물건 이름

    @Column(nullable = true)
    private String detail; // 물건 설명

    @Column(nullable = true)
    private Long category; // 물건 카테고리

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;

    public Item(Slot slot, String title, String detail, Image image, Long category) {
        this.slot = slot;
        this.title = title;
        this.image = image;
        this.category = category;
        this.createdAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
        this.updatedAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
    }
}

