package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class BoardAndImageDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddBoardAndImageRequest {
        private BoardDTO.AddBoardRequest board;

        private List<BoardImgDTO.AddBoardImgRequest> imgList = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailResponse {
        private BoardDTO.BoardDetailResponse board;
        private List<BoardImgDTO.SearchResponse> imgList  = new ArrayList<>();
    }
}
