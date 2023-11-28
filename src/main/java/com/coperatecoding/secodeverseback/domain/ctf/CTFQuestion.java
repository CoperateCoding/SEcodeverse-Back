package com.coperatecoding.secodeverseback.domain.ctf;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
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
