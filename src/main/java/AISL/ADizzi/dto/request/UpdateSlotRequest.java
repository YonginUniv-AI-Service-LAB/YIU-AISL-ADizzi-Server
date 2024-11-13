package AISL.ADizzi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "수납칸 수정 요청 정보")
public class UpdateSlotRequest {
    @NotBlank(message = "수납칸 이름은 필수 입력 값입니다.")
    @Schema(description = "이름")
    private String title;

    @Schema(description = "이미지 URL")
    private Long imageId;
}
