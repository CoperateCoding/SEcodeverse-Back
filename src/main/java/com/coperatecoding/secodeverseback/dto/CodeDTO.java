package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.CodeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
}
