package com.coperatecoding.secodeverseback.dto.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class QuestionAndTestAndImageDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddQuestionAndTestAndImageRequest{
        private QuestionDTO.AddQuestionRequest question;

        private List<TestCaseDTO.AddtestCaseRequest> testCase;

        private List<QuestionImgDTO.AddQuestionImgRequest> img =new ArrayList<>();;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionAndTest{
        private QuestionDTO.AddQuestionResponse question;

        private List<TestCaseDTO.SearchResponse> testCase;

        private List<QuestionImgDTO.SearchQuestionImgResponse> img =new ArrayList<>();;
    }

}
