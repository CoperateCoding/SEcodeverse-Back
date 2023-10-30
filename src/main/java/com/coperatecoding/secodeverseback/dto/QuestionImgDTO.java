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
    public static class SearchQuestionImgListRequest {
        private Long pk;
        private String imgUrl;

        public static SearchQuestionImgListRequest questionImg(Long imgPk,String url){
            return new SearchQuestionImgListRequest(imgPk,url);
        }

    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionImgListResponse {
        private Long pk;
        private String imgUrl;

    }




}
