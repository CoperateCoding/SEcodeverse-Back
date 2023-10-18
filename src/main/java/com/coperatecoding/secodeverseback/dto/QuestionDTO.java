package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
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
        private Long levelPk;
        private String title;
        private String intro;
        public static SearchQuestionListRequest questions(Long pk,Long levelPk ,String title, String intro ){
            return new SearchQuestionListRequest(pk,levelPk,title,intro);
        }

    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionListResponse {
        private Long pk;
        private Long levelPk;
        private String title;
        private String intro;

    }

}
