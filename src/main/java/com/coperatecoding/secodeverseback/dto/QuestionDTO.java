package com.coperatecoding.secodeverseback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class QuestionDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddQuestionRequest{
    @NotNull(message = "questionCategoryPk가 null 이면 안됩니다.")
    private Long categoryPk;
    @NotNull(message = "levelPk가 null이면 안됩니다.")
    private Long levelPk;
    @NotNull(message = "제목이 null이면 안됩니다")
    private String title;
    private String intro;
    @NotNull(message = "내용이 null이면 안됩니다.")
    private String content;
    private String limitations;
    private String source;
    @NotNull(message = "언어가 null이면 안됩니다")
    private String language;
    private String testcaseDescription;



    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionListRequest {
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String intro;
        private Long categoryPk;
        public static SearchQuestionListRequest questions(Long pk,String userName,Long levelPk ,String title, String intro , Long questionPk){
            return new SearchQuestionListRequest(pk,userName, levelPk, title, intro, questionPk);
        }

    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionResponse {
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String intro;
        private Long categoryPk;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularResponse {
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String categoryName;

    }

}
