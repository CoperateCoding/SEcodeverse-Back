package com.coperatecoding.secodeverseback.dto.ctf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CTFTeamQuestionDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamScoreByCategoryListResponse {
        private int categoryCnt; // 카테고리 개수
        private List<CTFTeamQuestionDTO.TeamScoreByCategoryResponse> list;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamScoreByCategoryResponse {
        private String categoryName;
        private int score;
    }

}
