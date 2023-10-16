package com.coperatecoding.secodeverseback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikesDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddLikeRequest{

        @NotNull(message = "게시글 정보가 null 이면 안됩니다.")
        private Long boardPK;



    }
}
