package com.coperatecoding.secodeverseback.dto.board;

import jakarta.validation.constraints.NotNull;
import lombok.*;


public class BoardImgDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddBoardImgRequest{
        @NotNull(message = "imgUrl이 null이면 안됩니다.")
        private String imgUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private Long pk;
        private String imgUrl;

        public static SearchRequest makeRequest(Long pk, String imgUrl) {
            return new SearchRequest(pk, imgUrl);
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private Long pk;
        private String imgUrl;

        public static SearchResponse makeResponse(Long pk, String imgUrl) {
            return new SearchResponse(pk, imgUrl);
        }
    }

}
