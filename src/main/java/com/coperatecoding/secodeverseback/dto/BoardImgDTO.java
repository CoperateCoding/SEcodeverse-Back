package com.coperatecoding.secodeverseback.dto;

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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchBoardImgListRequest {
        private Long pk;
        private String imgUrl;

        public static BoardImgDTO.SearchBoardImgListRequest boardImg(Long imgPk, String url){
            return new BoardImgDTO.SearchBoardImgListRequest(imgPk,url);
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchBoardImgListResponse {
        private Long pk;
        private String imgUrl;

    }

}
