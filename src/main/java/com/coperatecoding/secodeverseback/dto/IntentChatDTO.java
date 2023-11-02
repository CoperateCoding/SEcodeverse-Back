package com.coperatecoding.secodeverseback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IntentChatDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddIntentChatRequest{
        @NotNull(message ="input이 널이면 안됩니다")
        private String input;

        private int output;
    }
}
