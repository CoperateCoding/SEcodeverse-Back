package com.coperatecoding.secodeverseback.dto.ctf;

import com.coperatecoding.secodeverseback.domain.ctf.CTFLeagueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CTFTeamDTO {

    @Getter
    @NoArgsConstructor
    public static class AddRequest {
        private Long LeaguePk;
        private String name;
        private String pw;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private String name;
    }

}
