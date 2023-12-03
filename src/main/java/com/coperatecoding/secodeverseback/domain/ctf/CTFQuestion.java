package com.coperatecoding.secodeverseback.domain.ctf;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ctf_question")
public class CTFQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "league_pk")
    private CTFLeague league;

    @ManyToOne
    @JoinColumn(name = "category_pk")
    private CTFCategory category;

    @NotNull
    private String name;

    @NotNull
    private Integer score;

    @NotNull
    @Column(length = 99999)
    private String description;

    @NotNull
    private String answer;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CTFQuestionType type;

    @OneToMany(mappedBy = "ctfQuestion", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CTFImage> ctfImages = new ArrayList<>();

    public static CTFQuestion makeCTFQuestion(CTFLeague ctfLeague, CTFCategory ctfCategory, CTFQuestionType ctfQuestionType
            , String name, Integer score, String description, String answer) {
        CTFQuestion ctfQuestion = new CTFQuestion();
        ctfQuestion.league = ctfLeague;
        ctfQuestion.category = ctfCategory;
        ctfQuestion.type = ctfQuestionType;
        ctfQuestion.name = name;
        ctfQuestion.score = score;
        ctfQuestion.description = description;
        ctfQuestion.answer = answer;
        return ctfQuestion;
    }

    public void edit(CTFCategory ctfCategory, CTFQuestionType ctfQuestionType, String name,
                     Integer score, String description, String answer) {
        this.category = ctfCategory;
        this.type = ctfQuestionType;
        this.name = name;
        this.score = score;
        this.description = description;
        this.answer = answer;
    }
}
