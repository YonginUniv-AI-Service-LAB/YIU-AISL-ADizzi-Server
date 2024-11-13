package AISL.ADizzi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "비밀번호 수정 요청 정보")
public class UpdateMemberRequest {
    @Schema(description = "이메일")
    private String email;

    @Schema(description = "비밀번호")
    private String password;
}
