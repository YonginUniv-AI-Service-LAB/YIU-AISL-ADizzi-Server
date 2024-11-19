package AISL.ADizzi.dto.response;

import AISL.ADizzi.entity.Container;
import AISL.ADizzi.entity.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@Schema(description = "물건 정보")
public class ItemResponse {

    @Schema(description = "물건 아이디")
    private Long itemId;

    @Schema(description = "물건 이름")
    private String title;

    @Schema(description = "물건 설명")
    private String detail;

    @Schema(description = "물건 카테고리")
    private Long category;

    @Schema(description = "물건 이미지 URL")
    private String imageUrl;

    @Schema(description = "물건 수정 일시 yyyy-MM-dd HH:mm:ss 형식")
    private String updatedAt; // 수정 일시 yyyy-MM-dd HH:mm:ss

    public ItemResponse(Item item){
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.detail = item.getDetail();
        this.imageUrl = item.getImage().getImageUrl();
        this.updatedAt = item.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // yyyy-MM-dd HH:mm:ss 형식으로 변환
    }
}