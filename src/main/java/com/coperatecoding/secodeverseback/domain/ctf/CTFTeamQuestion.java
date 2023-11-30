package com.coperatecoding.secodeverseback.domain.ctf;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ctf_team_question")
public class CTFTeamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctf_question_pk")
    private CTFQuestion ctfQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctf_team_pk")
    private CTFTeam ctfTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctf_category_pk")
    private CTFCategory ctfCategory;

    @Column(name="total_score")
    private Integer totalScore;

    public static CTFTeamQuestion makeCTFTeamQuestion(CTFQuestion ctfQuestion, CTFTeam ctfTeam, CTFCategory ctfCategory, int score) {
        CTFTeamQuestion ctfTeamQuestion = new CTFTeamQuestion();
        ctfTeamQuestion.ctfQuestion = ctfQuestion;
        ctfTeamQuestion.ctfTeam = ctfTeam;
        ctfTeamQuestion.ctfCategory = ctfCategory;
        ctfTeamQuestion.totalScore = score;
        return ctfTeamQuestion;
    }

}
