package com.coperatecoding.secodeverseback.dto.question;

import jakarta.validation.constraints.NotNull;
import lombok.*;


public class LevelDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddCommentRequest{

        @NotNull(message = "레벨이 null이면 안됩니다.")
        private int score;
    }

}
