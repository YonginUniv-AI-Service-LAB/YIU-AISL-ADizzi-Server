package AISL.ADizzi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "방 수정 요청 정보")
public class UpdateRoomRequest {
    @Schema(description = "이름")
    private String title;

    @Schema(description = "아이콘")
    private Long icon;

    @Schema(description = "색상")
    private Long color;
}
