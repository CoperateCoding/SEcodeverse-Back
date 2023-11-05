package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import com.coperatecoding.secodeverseback.domain.board.BoardImage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @Builder
    public static class SearchListResponse {
        private int cnt;
        private List<SearchResponse> list;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchListRequest {
        //전부 nullable
        private Long categoryPk;
        private String q;
        @Builder.Default
        @Min(value = 2, message = "page 크기는 1보다 커야합니다")
        private Integer pageSize = 10;
        @Builder.Default
        @Min(value = 1, message = "page는 0보다 커야합니다")
        private Integer page = 1;
        private SortType sort;

        public static SearchListRequest makeRequest(Long categoryPk, String q, Integer pageSize, Integer page, SortType sort) {
            return new SearchListRequest(categoryPk, q, pageSize, page, sort);
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private Long pk;
        private String title;
        private String preview;
        private Long likeCnt;
        private Long commentCnt;
        private String createAt;
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
