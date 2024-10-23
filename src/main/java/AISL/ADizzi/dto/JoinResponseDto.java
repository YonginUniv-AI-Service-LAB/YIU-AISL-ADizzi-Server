package AISL.ADizzi.dto;

import AISL.ADizzi.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinResponseDto {
    private final Long userId;
    private final String email;

    @Builder
    public JoinResponseDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
    }
}
