package com.coperatecoding.secodeverseback.dto.ctf;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeagueStatus;
import com.coperatecoding.secodeverseback.dto.question.QuestionDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CTFLeagueDTO {

    @Getter
    @NoArgsConstructor
    public static class PostRequest {
        private String name;
        private LocalDateTime openTime;
        private LocalDateTime closeTime;
        private int memberCnt;
        private String notice;
        private String description;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private String name;
        private String openTime;
        private String closeTime;
        private int memberCnt;
        private String notice;
        private String description;
        private CTFLeagueStatus status;
    }

    @Getter
    @NoArgsConstructor
    public static class EditRequest {
        private String name;
        private String openTime;
        private String closeTime;
        private int memberCnt;
        private String notice;
        private String description;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BriefResponse {
        private String name;
        private String openTime;
        private String closeTime;
        private CTFLeagueStatus status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllListResponse {
        private int cnt;
        private List<BriefResponse> list;
    }



}
