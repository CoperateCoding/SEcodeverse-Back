package com.coperatecoding.secodeverseback.dto.ctf;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class CTFTeamDTO {

    @Getter
    @NoArgsConstructor
    public static class AddRequest {

        private Long leaguePk;

        @NotNull
        @Size(min = 2, max = 10, message = "팀명은 2에서 8자 사이 입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임은 한글 또는 영문이 필수이며, 숫자는 선택입니다.")
        private String name;

        @NotNull
        @Size(min=4, max=4)
        @Pattern(regexp = "^[0-9]{4}$", message = "숫자 4자리를 입력해주세요.")
        private String pw;
    }

    @Getter
    @NoArgsConstructor
    public static class JoinRequest {
        private String teamName;
        private String pw;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private String name;
        private Integer score;
        private Integer teamRank;
        private List<String> memberList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Top10TeamResponse {
        private String name;
        private Integer score;
        private Integer teamRank;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchListResponse {
        private int cnt;
        private List<CTFTeamDTO.SearchResponse> list;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Top10ListResponse {
        private int cnt;
        private List<CTFTeamDTO.Top10TeamResponse> list;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchResponse {
        private String name;
        private List<String> memberList;
        private int totalScore;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamRankListResponse {
        private int cnt;
        private List<CTFTeamDTO.TeamRankResponse> list;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamRankResponse {
        private String teamName;
        private Integer rank;
    }

}
