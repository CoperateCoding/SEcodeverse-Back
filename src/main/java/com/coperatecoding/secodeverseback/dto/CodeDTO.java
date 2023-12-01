package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.CodeStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public class CodeDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCodeListRequest{
        private Long pk;
        private CodeStatus codeStatus;
        private Long questionPk;
        public static SearchCodeListRequest Codes(Long pk, CodeStatus CodeStatus, Long questionPk){
            return new SearchCodeListRequest(pk,CodeStatus,questionPk);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchCodeListResponse{
        private Long pk;
        private CodeStatus codeStatus;
        private Long questionPk;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCodeRequest {
        private CodeStatus codeStatus;
        private String content;
        private String compileTime;
        private Long memory;
        private Double accuracy;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageableCodeListRequest{
        private int cnt;
        private Long pk;
        private CodeStatus codeStatus;
        private Long questionPk;
        public static PageableCodeListRequest Codes(int cnt,Long pk, CodeStatus CodeStatus, Long questionPk){
            return new PageableCodeListRequest(cnt ,pk,CodeStatus,questionPk);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageableCodeListResponse{
        private int cnt;
        private Long pk;
        private CodeStatus codeStatus;
        private Long questionPk;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyTrueQuestionResponseList {
        private int cnt;
        private List<CodeDTO.MyTrueQuestionResponse> list;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MyTrueQuestionResponse {
        private int day; // 날자
        private int cnt; // 맞춘 개수
    }

    public class CalendarRequest {
        private int year; // 년도
        private int month; // 월
    }
}
