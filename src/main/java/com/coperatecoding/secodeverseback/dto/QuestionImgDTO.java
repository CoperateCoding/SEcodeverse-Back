package com.coperatecoding.secodeverseback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class QuestionImgDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddQuestionImgRequest{
        @NotNull(message = "imgUrl이 null이면 안됩니다.")
        private String imgUrl;

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionImgRequest {
        private Long pk;
        private String imgUrl;

        public static SearchQuestionImgRequest questionImg(Long imgPk, String url){
            return new SearchQuestionImgRequest(imgPk,url);
        }

    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionImgResponse {
        private Long pk;
        private String imgUrl;

    }




}
