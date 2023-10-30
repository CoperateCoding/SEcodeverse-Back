package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class CommentDTO {



    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddCommentRequest{

        @NotNull(message = "게시글 정보가 null 이면 안됩니다.")
        private Long boardPK;

        @NotNull(message = "댓글 내용이 null이면 안됩니다.")
        private String content;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchListRequest {
        private Long pk;
        private LocalDateTime createAt;
        private String content;
        private User user;

        public static SearchListRequest makeRequest(Long pk,LocalDateTime createAt, String content, User user) {
            return new SearchListRequest(pk, createAt,content,user);
        }
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private Long pk;
        private LocalDateTime createAt;
        private String content;
        private User user;

    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class modifyRequest{
        private String content;}



}
