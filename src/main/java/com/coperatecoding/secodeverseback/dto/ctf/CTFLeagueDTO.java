package com.coperatecoding.secodeverseback.dto.ctf;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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


}
