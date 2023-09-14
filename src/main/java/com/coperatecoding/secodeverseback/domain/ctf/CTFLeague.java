package com.coperatecoding.secodeverseback.domain.ctf;

import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ctf_league")
public class CTFLeague {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @OneToMany(mappedBy = "ctf_league", cascade = CascadeType.PERSIST)
    private List<CTFQuestion> ctfQuestions = new ArrayList<>();

    @NotNull
    private String name;

    @Column(name = "short_description")
    private String shortDescription;

    @NotNull
    @CreationTimestamp
    @Column(name = "open_time")
    private LocalDateTime openTime;

    @NotNull
    @CreationTimestamp
    @Column(name = "open_time")
    private LocalDateTime closeTime;

    @NotNull
    @Column(name = "member_cnt")
    private int memberCnt;

    private String notice;

    @Lob
    @Column(name = "detail_description")
    private String detailDescription;

    @NotNull
    private LeagueStatus status;

}
