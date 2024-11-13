package AISL.ADizzi.dto.response;

import AISL.ADizzi.entity.Slot;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@Schema(description = "수납칸 정보")
public class SlotResponse {

    @Schema(description = "수납칸 아이디")
    private Long slotId;

    @Schema(description = "수납칸 이름")
    private String title;

    @Schema(description = "수납칸 이미지 URL")
    private String imageUrl;

    @Schema(description = "수납칸 수정 일시 yyyy-MM-dd HH:mm:ss 형식")
    private String updatedAt; // 수정 일시 yyyy-MM-dd HH:mm:ss

    public SlotResponse(Slot slot){
        this.slotId = slot.getId();
        this.title = slot.getTitle();
        this.imageUrl = slot.getImage().getImageUrl();
        this.updatedAt = slot.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // yyyy-MM-dd HH:mm:ss 형식으로 변환
    }
}
