package com.coperatecoding.secodeverseback.dto.question;

import com.coperatecoding.secodeverseback.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class LevelDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddCommentRequest{

        @NotNull(message = "레벨이 null이면 안됩니다.")
        private int score;


    }
}
