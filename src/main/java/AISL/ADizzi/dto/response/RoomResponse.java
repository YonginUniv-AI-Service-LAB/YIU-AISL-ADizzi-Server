package AISL.ADizzi.dto.response;

import AISL.ADizzi.entity.Item;
import AISL.ADizzi.entity.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@Schema(description = "방 정보")
public class RoomResponse {
    @Schema(description = "방 아이디")
    private Long roomId;

    @Schema(description = "방 이름")
    private String title;

    @Schema(description = "방 아이콘")
    private Long icon;

    @Schema(description = "방 색상")
    private Long color;

    @Schema(description = "방 수정 일시 yyyy-MM-dd HH:mm:ss 형식")
    private String updatedAt; // 수정 일시 yyyy-MM-dd HH:mm:ss

    public RoomResponse(Room room){
        this.roomId = room.getId();
        this.title = room.getTitle();
        this.icon = room.getIcon();
        this.color = room.getColor();
        this.updatedAt = room.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // yyyy-MM-dd HH:mm:ss 형식으로 변환
    }
}
