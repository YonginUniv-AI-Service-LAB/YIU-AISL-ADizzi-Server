package AISL.ADizzi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "수납칸 수정 요청 정보")
public class UpdateSlotRequest {
    @Schema(description = "이름")
    private String title;

    @Schema(description = "이미지 URL")
    private Long imageId;
}
