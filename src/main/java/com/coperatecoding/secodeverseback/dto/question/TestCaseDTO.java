package com.coperatecoding.secodeverseback.dto.question;

import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchListRequest {
        private Long pk;
        private String input;
        private String output;

        public static TestCaseDTO.SearchListRequest makeRequest(Long pk,String input, String output) {
            return new TestCaseDTO.SearchListRequest(pk, input, output);
        }
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private Long pk;
        private String input;
        private String output;
    }


}
