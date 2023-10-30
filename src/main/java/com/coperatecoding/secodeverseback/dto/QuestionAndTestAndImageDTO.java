package com.coperatecoding.secodeverseback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
