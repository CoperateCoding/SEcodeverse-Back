package com.coperatecoding.secodeverseback.dto.ctf;

import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
