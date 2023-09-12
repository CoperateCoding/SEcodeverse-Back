package com.coperatecoding.secodeverseback.domain.question;

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
@Table(name = "question_category")
public class QuestionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private String name;

    public static QuestionCategory makeCategory(String name) {
        QuestionCategory newCategory = new QuestionCategory();
        newCategory.name  = name;
        return newCategory;
    }

    public void updateName(String name) { this.name = name; }
}
