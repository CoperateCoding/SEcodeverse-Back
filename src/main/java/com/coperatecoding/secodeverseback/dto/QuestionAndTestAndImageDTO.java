package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.question.Question;
import lombok.*;

import java.util.List;

public class QuestionAndTestAndImageDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddQuestionAndTestAndImageRequest{
        private QuestionDTO.AddQuestionRequest question;

        private List<TestCaseDTO.AddtestCaseRequest> testCase;

        private List<QuestionImgDTO.AddQuestionImgRequest> img;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionAndTest{
        private Question question;

        private List<TestCaseDTO.SearchResponse> testCase;

        private List<QuestionImgDTO.SearchQuestionImgListResponse> img;
    }

}
