package AISL.ADizzi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "방 생성 요청 정보")
public class CreateRoomRequest {
    @NotBlank(message = "방 이름은 필수 입력 값입니다.")
    @Schema(description = "이름")
    private String title;

    @Schema(description = "아이콘")
    private Long icon;

    @Schema(description = "색상")
    private Long color;
}
