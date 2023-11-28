package com.coperatecoding.secodeverseback.dto.ctf;

import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CTFQuestionDTO {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostRequest {
        private Long leaguePk;
        private Long categoryPk;
        private CTFQuestionType ctfQuestionType;
        private String name;
        private Integer score;
        private String description;
        private String answer;
        private String[] imgUrlList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EditRequest {
        private Long categoryPk;
        private CTFQuestionType ctfQuestionType;
        private String name;
        private Integer score;
        private String description;
        private String answer;
        private String[] imgUrlList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllListResponse {
        private int cnt;
        private List<CTFQuestionDTO.BriefResponse> list;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BriefResponse {
        private Long questionPk;
        private String questionName;
        private Integer score;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private String questionName;
        private String description;
        private CTFQuestionType ctfQuestionType;
        private Integer score;
        private String[] imgUrlList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SolveRequest {
        private String answer;
    }
}
