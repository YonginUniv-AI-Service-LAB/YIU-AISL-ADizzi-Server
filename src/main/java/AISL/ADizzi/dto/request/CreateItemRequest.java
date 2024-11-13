package AISL.ADizzi.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "물건 생성 요청 정보")
public class CreateItemRequest {
    @NotBlank(message = "물건 이름은 필수 입력 값입니다.")
    @Schema(description = "이름")
    private String title;

    @Schema(description = "설명")
    private String detail;

    @Schema(description = "카테고리")
    private Long category;

    @Schema(description = "이미지 URL")
    private Long imageId;
}
