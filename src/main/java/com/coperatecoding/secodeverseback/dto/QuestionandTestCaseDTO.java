package com.coperatecoding.secodeverseback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

public class QuestionandTestCaseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddQuestionAndTestRequest{
        private QuestionDTO.AddQuestionRequest question;

        private List<TestCaseDTO.AddtestCaseRequest> testCase;

    }
}
