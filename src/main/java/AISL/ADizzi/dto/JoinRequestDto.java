package AISL.ADizzi.dto;

import AISL.ADizzi.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequestDto {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}

