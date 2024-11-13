package AISL.ADizzi.dto.response;

import AISL.ADizzi.entity.Container;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@Schema(description = "수납장 정보")
public class ContainerResponse {

    @Schema(description = "수납장 아이디")
    private Long containerId;

    @Schema(description = "수납장 이름")
    private String title;

    @Schema(description = "수납장 이미지 URL")
    private String imageUrl;

    @Schema(description = "수납장 수정 일시 yyyy-MM-dd HH:mm:ss 형식")
    private String updatedAt; // 수정 일시 yyyy-MM-dd HH:mm:ss

    public ContainerResponse(Container container){
        this.containerId = container.getId();
        this.title = container.getTitle();
        this.imageUrl = container.getImage().getImageUrl();
        this.updatedAt = container.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // yyyy-MM-dd HH:mm:ss 형식으로 변환
    }
}
