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
@Table(name = "container")
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 수납장 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // 수납장이 위치한 방

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 일시 yyyy-MM-dd HH:mm:ss

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 일시 yyyy-MM-dd HH:mm:ss

    @Column(nullable = false)
    private String title; // 수납장 이름

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;

    @OneToMany(mappedBy = "container", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Slot> slots; // 방에 저장된 수납장 목록

    @Column(nullable = true)
    private Long slotId; // 기본 수납칸 아이디

    public Container(Room room, String title, Image image) {
        this.room = room;
        this.title = title;
        this.image = image;
        this.createdAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
        this.updatedAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
    }
}

