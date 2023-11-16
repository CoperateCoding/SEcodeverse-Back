package com.coperatecoding.secodeverseback.domain;

import com.coperatecoding.secodeverseback.domain.question.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "test_case")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_pk")
    private Question question;

    @Column(columnDefinition = "LONGTEXT")
    private String input;

    @Column(columnDefinition = "LONGTEXT")
    @NotNull
    private String output;


    public static TestCase makeTestCase(Question question,String input, String output) {
        TestCase testCase = new TestCase();
        testCase.question=question;
        testCase.input = input;
        testCase.output = output;
        return testCase;
    }


    public void updateTestCase(String input, String output) {
        this.input = input;
        this.output = output;
    }
}
