package AISL.ADizzi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoveItemRequest {

    @Schema(description = "이름")
    private String title;

    @Schema(description = "설명")
    private String detail;

    @Schema(description = "카테고리")
    private Long category;

    @Schema(description = "이미지 URL")
    private Long imageId;
}
