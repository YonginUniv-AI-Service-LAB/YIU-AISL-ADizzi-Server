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
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 방 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member; // 방 작성자

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 일시 yyyy-MM-dd HH:mm:ss

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정 일시 yyyy-MM-dd HH:mm:ss

    @Column(nullable = false)
    private String title; // 방 이름

    @Column(nullable = true)
    private Long icon; // 방 아이콘

    @Column(nullable = true)
    private Long color; // 방 색

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Container> containers; // 방에 저장된 수납장 목록

    public Room(Member member, String title, Long icon, Long color) {
        this.member = member;
        this.title = title;
        this.icon = icon;
        this.color = color;
        this.createdAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
        this.updatedAt = LocalDateTime.now(); // 객체 생성 시 현재 시간으로 설정
    }
}

