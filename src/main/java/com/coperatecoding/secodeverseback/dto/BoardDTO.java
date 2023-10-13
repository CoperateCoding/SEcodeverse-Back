package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import com.coperatecoding.secodeverseback.domain.board.BoardImage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class BoardDTO {
    
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddBoardRequest {

        @NotNull(message = "게시글 정보 카테고리가 null이면 안됩니다")
        private Long categoryPk;

        @NotNull(message = "게시글 정보 title이 null이면 안됩니다")
        private String title;

        @NotNull(message = "게시글 정보 내용이 null이면 안됩니다")
        private String content;

//        private List<BoardImage> imageList = new ArrayList<>();

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchListResponse {
        private int cnt;
        private List<SearchResponse> searchList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private Long pk;
        private String title;
        private String preview;
        private Long likeCnt;
        private Long commentCnt;
        private String updateAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardDetailResponse {
        private Long writerPk;
        private String profileUrl;
        private BoardCategory category;
        private String updateAt;
        private Long likeCnt;
        private Long commentCnt;
        private String title;
        private String content;
        private List<BoardImage> imageList = new ArrayList<>();

    }

}
