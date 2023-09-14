package com.coperatecoding.secodeverseback.domain.ctf;

import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_pk", referencedColumnName = "pk")
    private CTFLeague league;

    @OneToMany(mappedBy = "ctf_question", cascade = CascadeType.PERSIST)
    private List<CTFCategory> ctfCategories = new ArrayList<>();

    @NotNull
    private String name;

    @NotNull
    private int score;

    @NotNull
    @Lob
    private String description;

    @NotNull
    private String answer;

    @NotNull
    private CTFQuestionType type;

}
