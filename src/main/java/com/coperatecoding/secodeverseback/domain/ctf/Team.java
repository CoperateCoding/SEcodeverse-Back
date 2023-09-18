package com.coperatecoding.secodeverseback.domain.ctf;

import com.coperatecoding.secodeverseback.domain.Users;
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
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_pk", referencedColumnName = "pk")
    private CTFLeague ctfLeague;

    @NotNull
    private String name;

    @NotNull
    private String pw;

    private Integer score;

    @Column(name = "team_rank")
    private Integer teamRank;

    @OneToMany(mappedBy = "team")
    private List<Users> users = new ArrayList<>();
}
