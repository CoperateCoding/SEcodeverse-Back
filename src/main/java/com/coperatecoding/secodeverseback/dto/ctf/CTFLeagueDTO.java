package com.coperatecoding.secodeverseback.dto.ctf;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeagueStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CTFLeagueDTO {

    @Getter
    @NoArgsConstructor
    public static class AddLeagueRequest {
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
    public static class CTFLeagueDetailResponse {
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
    public static class CTFListAllResponse {
        private int cnt;
        private List<BriefResponse> list;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OngoingResponse {
        private Long leaguePk;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusResponse {
        private CTFLeagueStatus ctfLeagueStatus;
    }
}
