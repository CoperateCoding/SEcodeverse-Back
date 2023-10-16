package com.coperatecoding.secodeverseback.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddCommentRequest{


        @NotNull(message = "게시글 정보가 null 이면 안됩니다.")
        private Long boardPK;

        @NotNull(message = "댓글 내용이 null이면 안됩니다.")
        private String content;



    }


}
