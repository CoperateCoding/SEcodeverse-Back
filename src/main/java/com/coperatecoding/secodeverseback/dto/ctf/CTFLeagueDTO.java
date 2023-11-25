package com.coperatecoding.secodeverseback.dto.ctf;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeagueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CTFLeagueDTO {

    @Getter
    @NoArgsConstructor
    public static class AddRequest {

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


}
