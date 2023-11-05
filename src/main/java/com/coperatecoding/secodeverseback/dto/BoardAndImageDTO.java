package com.coperatecoding.secodeverseback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BoardAndImageDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddBoardAndImageRequest {
        private BoardDTO.AddBoardRequest board;

        private List<BoardImgDTO.AddBoardImgRequest> imgList;
    }
}
