package com.coperatecoding.secodeverseback.dto.question;

import lombok.*;

public class CategoryDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuestionCategoryResponse {
        private Long pk;
        private String categoryName;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryRequest {

     private Long pk;
     private String categoryName;

        public static CategoryDTO.CategoryRequest category(Long pk, String categoryName) {
            return new CategoryDTO.CategoryRequest( pk,  categoryName);
        }

    }
}
