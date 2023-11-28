package com.coperatecoding.secodeverseback.domain.ctf;

import com.coperatecoding.secodeverseback.domain.User;
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
@Table(name = "ctf_team")
public class CTFTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_pk", referencedColumnName = "pk")
    private CTFLeague league;

    @NotNull
    @Column(length = 8)
    private String name;

    @NotNull
    @Column(length = 20)
    private String pw;

    private Integer score;

    @Column(name = "team_rank")
    private Integer teamRank;

    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<>();

    public static CTFTeam makeCTFTeam(CTFLeague league, String name, String pw) {
        CTFTeam ctfTeam = new CTFTeam();
        ctfTeam.league = league;
        ctfTeam.name = name;
        ctfTeam.pw = pw;
        return ctfTeam;
    }

    public void addScore(Integer score) {
        if (this.score == null) {
            this.score = score;
        } else {
            this.score += score;
        }
    }
}
