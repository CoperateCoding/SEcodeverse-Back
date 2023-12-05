package com.coperatecoding.secodeverseback.dto.question;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

public class QuestionDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddQuestionRequest {
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
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddQuestionResponse {
        private Long pk;
        private Long levelPk;
        private String title;
        private String intro;
        private String content;
        private String limitations;
        private String source;
        private String language;
        private String testcaseDescription;
        private Long categoryPk;

        public static QuestionDTO.AddQuestionResponse makeResponse(Long pk, Long levelPk, String title, String intro, String content, String limitations ,String source, String language, String testcaseDescription,Long categoryPk) {
            return new QuestionDTO.AddQuestionResponse(pk, levelPk,title,intro,content,limitations,source,language,testcaseDescription,categoryPk);
        }
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

        public static SearchQuestionListRequest questions(Long pk, String userName, Long levelPk, String title, String intro, Long questionPk) {
            return new SearchQuestionListRequest(pk, userName, levelPk, title, intro, questionPk);
        }

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class questionPagingRequest {
        private int cnt;
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private Long categoryPk;

        public static questionPagingRequest questions(int cnt,Long pk, String userName, Long levelPk, String title, Long questionPk) {
            return new questionPagingRequest(cnt, pk, userName, levelPk, title, questionPk);
        }

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class questionCategoryNameRequest {

        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String intro;
        private String categoryName;

        public static questionCategoryNameRequest questions(Long pk, String userName, Long levelPk, String title, String intro,String categoryName) {
            return new questionCategoryNameRequest(pk, userName, levelPk, title, intro,categoryName);
        }

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class questionCategoryNamePagingRequest {
        private int cnt;
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String categoryName;

        public static questionCategoryNamePagingRequest questions(int cnt,Long pk, String userName, Long levelPk, String title, String categoryName) {
            return new questionCategoryNamePagingRequest(cnt, pk, userName, levelPk, title, categoryName);
        }

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class questionListName {
        private int cnt;
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String  categoryName;

        public static questionCategoryNamePagingRequest questions(int cnt,Long pk, String userName, Long levelPk, String title,String categoryName) {
            return new questionCategoryNamePagingRequest(cnt, pk, userName, levelPk, title, categoryName);
        }

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class questionPagingResponse {
        private int cnt;
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
    public static class SearchQuestionResponse {
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String intro;
        private Long categoryPk;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SearchQuestionResponse that = (SearchQuestionResponse) o;
            return Objects.equals(pk, that.pk);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pk);
        }


    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionCategoryNameResponse {
        private Long pk;
        private String userName;
        private Long levelPk;
        private String title;
        private String intro;
        private String categoryName;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchListResponse {
        private int cnt;
        private List<QuestionDTO.SearchQuestionResponse> list;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchListNameResponse {
        private int cnt;
        private List<QuestionDTO.SearchQuestionCategoryNameResponse> list;
    }

}

