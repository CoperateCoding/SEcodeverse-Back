package com.coperatecoding.secodeverseback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;


public class TestCaseDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddtestCaseRequestList{
        private List<AddtestCaseRequest> list;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddtestCaseRequest{

        private String input;
        @NotNull(message ="output이 null 이면 안됩니다.")
        private String output;
    }


}
