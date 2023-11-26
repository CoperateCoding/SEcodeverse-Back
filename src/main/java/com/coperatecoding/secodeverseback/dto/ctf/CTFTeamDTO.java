package com.coperatecoding.secodeverseback.dto.ctf;

import com.coperatecoding.secodeverseback.dto.board.BoardDTO;
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
        private String name;
        private String pw;
    }

    @Getter
    @NoArgsConstructor
    public static class JoinRequest {
        private Long teamPk;
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
        private List<CTFTeamDTO.DetailResponse> list;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchResponse {
        private String name;
        private List<String> memberList;
    }
}
